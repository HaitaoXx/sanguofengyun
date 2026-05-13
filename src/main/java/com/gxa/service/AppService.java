package com.gxa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxa.domain.dto.AppCreateDTO;
import com.gxa.domain.dto.AppUpdateDTO;
import com.gxa.domain.entity.App;
import com.gxa.domain.entity.Question;
import com.gxa.domain.vo.AppVO;
import com.gxa.domain.vo.QuestionVO;
import com.gxa.mapper.AppMapper;
import com.gxa.mapper.QuestionMapper;
import com.gxa.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AppService {
    
    @Autowired
    private AppMapper appMapper;
    
    @Autowired
    private QuestionMapper questionMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired(required = false)
    private RedissonClient redissonClient;
    
    private static final String APP_CACHE_PREFIX = "app:";
    private static final long CACHE_EXPIRE_SECONDS = 3600;
    
    private boolean isRedisAvailable() {
        return redisTemplate != null && redissonClient != null;
    }
    
    @Transactional
    public AppVO createApp(AppCreateDTO dto, Long userId) {
        App app = new App();
        app.setAppId(generateAppId());
        app.setUserId(userId);
        app.setTitle(dto.getTitle());
        app.setDescription(dto.getDescription());
        app.setType(dto.getType());
        app.setCoverImage(dto.getCoverImage());
        app.setConfigJson(dto.getConfigJson());
        app.setStatus(0);
        app.setViewCount(0);
        app.setAnswerCount(0);
        app.setShareCount(0);
        app.setIsAiGenerated(0);
        
        appMapper.insertApp(app);
        
        return convertToVO(app);
    }
    
    @Transactional
    public AppVO updateApp(AppUpdateDTO dto) {
        App app = appMapper.selectByAppId(dto.getAppId());
        if (app == null) {
            throw new RuntimeException("应用不存在");
        }
        
        boolean needReaudit = false;
        
        if (dto.getTitle() != null && !dto.getTitle().equals(app.getTitle())) {
            app.setTitle(dto.getTitle());
            needReaudit = true;
        }
        if (dto.getDescription() != null && !dto.getDescription().equals(app.getDescription())) {
            app.setDescription(dto.getDescription());
            needReaudit = true;
        }
        if (dto.getCoverImage() != null && !dto.getCoverImage().equals(app.getCoverImage())) {
            app.setCoverImage(dto.getCoverImage());
            needReaudit = true;
        }
        if (dto.getConfigJson() != null) {
            app.setConfigJson(dto.getConfigJson());
            needReaudit = true;
        }
        
        if (needReaudit && app.getStatus() == 1) {
            app.setStatus(0);
        }
        
        appMapper.updateById(app);
        
        if (dto.getQuestions() != null && !dto.getQuestions().isEmpty()) {
            updateQuestions(dto.getAppId(), dto.getQuestions());
            if (app.getStatus() == 1) {
                app.setStatus(0);
                appMapper.updateById(app);
            }
        }
        
        clearAppCache(dto.getAppId());
        
        return convertToVO(app);
    }
    
    @Transactional
    public void updateQuestions(String appId, List<com.gxa.domain.dto.QuestionDTO> questionDTOs) {
        questionMapper.delete(new LambdaQueryWrapper<Question>().eq(Question::getAppId, appId));
        
        for (com.gxa.domain.dto.QuestionDTO dto : questionDTOs) {
            Question question = new Question();
            question.setAppId(appId);
            question.setQuestionText(dto.getQuestionText());
            question.setQuestionType(dto.getQuestionType());
            question.setOptions(dto.getOptions());
            question.setScore(dto.getScore());
            question.setOrderNum(dto.getOrderNum());
            question.setConfigJson(dto.getConfigJson());
            questionMapper.insert(question);
        }
    }
    
    public AppVO getAppByAppId(String appId) {
        if (isRedisAvailable()) {
            String cacheKey = APP_CACHE_PREFIX + appId;
            AppVO cachedVO = (AppVO) redisTemplate.opsForValue().get(cacheKey);
            if (cachedVO != null) {
                return cachedVO;
            }
            
            String lockKey = "lock:app:" + appId;
            RLock lock = redissonClient.getLock(lockKey);
            
            try {
                lock.lock(10, TimeUnit.SECONDS);
                
                cachedVO = (AppVO) redisTemplate.opsForValue().get(cacheKey);
                if (cachedVO != null) {
                    return cachedVO;
                }
                
                AppVO vo = getAppVOFromDatabase(appId);
                redisTemplate.opsForValue().set(cacheKey, vo, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
                return vo;
            } finally {
                lock.unlock();
            }
        } else {
            return getAppVOFromDatabase(appId);
        }
    }
    
    private AppVO getAppVOFromDatabase(String appId) {
        App app = appMapper.selectByAppId(appId);
        if (app == null) {
            throw new RuntimeException("应用不存在");
        }
        
        AppVO vo = convertToVO(app);
        List<Question> questions = questionMapper.selectByAppId(appId);
        vo.setQuestions(questions.stream().map(this::convertQuestionToVO).collect(Collectors.toList()));
        
        return vo;
    }
    
    public IPage<AppVO> getPublishedApps(Integer pageNum, Integer pageSize, String keyword) {
        Page<App> page = new Page<>(pageNum, pageSize);
        IPage<App> appPage = appMapper.selectPublishedApps(page, 1, keyword);
        
        Page<AppVO> voPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        List<AppVO> voList = appPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    public IPage<AppVO> getUserApps(Long userId, Integer pageNum, Integer pageSize) {
        Page<App> page = new Page<>(pageNum, pageSize);
        IPage<App> appPage = appMapper.selectPage(page, new LambdaQueryWrapper<App>()
            .eq(App::getUserId, userId)
            .orderByDesc(App::getCreateTime));
        
        Page<AppVO> voPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        List<AppVO> voList = appPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    @Transactional
    public void incrementViewCount(String appId) {
        App app = appMapper.selectByAppId(appId);
        if (app != null) {
            app.setViewCount(app.getViewCount() + 1);
            appMapper.updateById(app);
            clearAppCache(appId);
        }
    }
    
    @Transactional
    public void incrementAnswerCount(String appId) {
        App app = appMapper.selectByAppId(appId);
        if (app != null) {
            app.setAnswerCount(app.getAnswerCount() + 1);
            appMapper.updateById(app);
            clearAppCache(appId);
        }
    }
    
    @Transactional
    public void incrementShareCount(String appId) {
        App app = appMapper.selectByAppId(appId);
        if (app != null) {
            app.setShareCount(app.getShareCount() + 1);
            appMapper.updateById(app);
            clearAppCache(appId);
        }
    }
    
    @Transactional
    public void deleteApp(String appId) {
        appMapper.delete(new LambdaQueryWrapper<App>().eq(App::getAppId, appId));
        questionMapper.delete(new LambdaQueryWrapper<Question>().eq(Question::getAppId, appId));
        clearAppCache(appId);
    }
    
    private AppVO convertToVO(App app) {
        AppVO vo = new AppVO();
        BeanUtils.copyProperties(app, vo);
        return vo;
    }
    
    private QuestionVO convertQuestionToVO(Question question) {
        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);
        return vo;
    }
    
    private String generateAppId() {
        return "APP" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private void clearAppCache(String appId) {
        if (isRedisAvailable()) {
            String cacheKey = APP_CACHE_PREFIX + appId;
            redisTemplate.delete(cacheKey);
        }
    }
}
