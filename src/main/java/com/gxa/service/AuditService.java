package com.gxa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxa.domain.dto.AppAuditDTO;
import com.gxa.domain.entity.App;
import com.gxa.domain.entity.AppAudit;
import com.gxa.domain.vo.AppVO;
import com.gxa.mapper.AppAuditMapper;
import com.gxa.mapper.AppMapper;
import com.gxa.mapper.QuestionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuditService {
    
    @Autowired
    private AppAuditMapper appAuditMapper;
    
    @Autowired
    private AppMapper appMapper;
    
    @Autowired
    private QuestionMapper questionMapper;
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;
    
    private boolean isRedisAvailable() {
        return redisTemplate != null;
    }
    
    @Transactional
    public void auditApp(AppAuditDTO dto, Long auditorId) {
        App app = appMapper.selectByAppId(dto.getAppId());
        if (app == null) {
            throw new RuntimeException("应用不存在");
        }
        
        AppAudit audit = new AppAudit();
        audit.setAppId(dto.getAppId());
        audit.setAuditorId(auditorId);
        audit.setAuditStatus(dto.getAuditStatus());
        audit.setAuditComment(dto.getAuditComment());
        audit.setAuditTime(LocalDateTime.now());
        appAuditMapper.insert(audit);
        
        app.setStatus(dto.getAuditStatus());
        appMapper.updateById(app);
        
        clearAppCache(dto.getAppId());
    }
    
    public List<AppVO> getPendingApps() {
        List<App> apps = appMapper.selectList(new LambdaQueryWrapper<App>()
            .eq(App::getStatus, 0)
            .orderByDesc(App::getCreateTime));
        
        return apps.stream().map(app -> {
            AppVO vo = new AppVO();
            BeanUtils.copyProperties(app, vo);
            return vo;
        }).collect(Collectors.toList());
    }
    
    public List<AppVO> getAuditedApps(Integer auditStatus) {
        List<App> apps = appMapper.selectList(new LambdaQueryWrapper<App>()
            .eq(App::getStatus, auditStatus)
            .orderByDesc(App::getUpdateTime));
        
        return apps.stream().map(app -> {
            AppVO vo = new AppVO();
            BeanUtils.copyProperties(app, vo);
            return vo;
        }).collect(Collectors.toList());
    }
    
    public List<AppAudit> getAuditHistory(String appId) {
        return appAuditMapper.selectList(new LambdaQueryWrapper<AppAudit>()
            .eq(AppAudit::getAppId, appId)
            .orderByDesc(AppAudit::getAuditTime));
    }
    
    private void clearAppCache(String appId) {
        if (isRedisAvailable()) {
            String cacheKey = "app:" + appId;
            redisTemplate.delete(cacheKey);
        }
    }
}
