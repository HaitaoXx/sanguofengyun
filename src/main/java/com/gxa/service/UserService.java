package com.gxa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxa.domain.dto.UserLoginDTO;
import com.gxa.domain.dto.UserRegisterDTO;
import com.gxa.domain.dto.UserUpdateDTO;
import com.gxa.domain.entity.AnswerDetail;
import com.gxa.domain.entity.App;
import com.gxa.domain.entity.User;
import com.gxa.domain.entity.UserAnswer;
import com.gxa.domain.vo.AchievementVO;
import com.gxa.domain.vo.AnswerDetailVO;
import com.gxa.domain.vo.UserAnswerHistoryVO;
import com.gxa.domain.vo.UserStatisticsVO;
import com.gxa.domain.vo.UserVO;
import com.gxa.mapper.AnswerDetailMapper;
import com.gxa.mapper.AppMapper;
import com.gxa.mapper.QuestionMapper;
import com.gxa.mapper.UserAnswerMapper;
import com.gxa.mapper.UserMapper;
import com.gxa.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserAnswerMapper userAnswerMapper;
    
    @Autowired
    private AnswerDetailMapper answerDetailMapper;
    
    @Autowired
    private AppMapper appMapper;
    
    @Autowired
    private QuestionMapper questionMapper;
    
    public UserVO login(UserLoginDTO dto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
            .eq(User::getUsername, dto.getUsername())
            .eq(User::getStatus, 1));
        
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        if (!dto.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        vo.setToken(token);
        return vo;
    }
    
    public UserVO register(UserRegisterDTO dto) {
        User existing = userMapper.selectOne(new LambdaQueryWrapper<User>()
            .eq(User::getUsername, dto.getUsername()));
        
        if (existing != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(0);
        user.setStatus(1);
        
        userMapper.insert(user);
        
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
    
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
    
    public UserVO updateUser(Long userId, UserUpdateDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (dto.getNickname() != null && !dto.getNickname().trim().isEmpty()) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            Integer count = userMapper.countByEmail(dto.getEmail(), userId);
            if (count > 0) {
                throw new RuntimeException("邮箱已被使用");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            Integer count = userMapper.countByPhone(dto.getPhone(), userId);
            if (count > 0) {
                throw new RuntimeException("手机号已被使用");
            }
            user.setPhone(dto.getPhone());
        }
        if (dto.getOldPassword() != null && dto.getNewPassword() != null) {
            if (!dto.getOldPassword().equals(user.getPassword())) {
                throw new RuntimeException("原密码错误");
            }
            user.setPassword(dto.getNewPassword());
        }
        
        userMapper.updateById(user);
        
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
    
    public List<UserAnswerHistoryVO> getUserAnswerHistory(Long userId, Integer page, Integer size) {
        LambdaQueryWrapper<UserAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAnswer::getUserId, userId);
        wrapper.orderByDesc(UserAnswer::getCreateTime);
        wrapper.last("LIMIT " + (page - 1) * size + ", " + size);
        
        List<UserAnswer> userAnswers = userAnswerMapper.selectList(wrapper);
        
        return userAnswers.stream().map(userAnswer -> {
            UserAnswerHistoryVO vo = new UserAnswerHistoryVO();
            vo.setAnswerId(userAnswer.getAnswerId());
            vo.setAppId(userAnswer.getAppId());
            
            App app = appMapper.selectByAppId(userAnswer.getAppId());
            if (app != null) {
                vo.setAppName(app.getTitle());
            }
            
            vo.setScore(userAnswer.getScore());
            vo.setSubmitTime(userAnswer.getCreateTime());
            
            List<AnswerDetail> details = answerDetailMapper.selectByAnswerId(userAnswer.getAnswerId());
            vo.setTotalQuestions(details.size());
            vo.setCorrectCount((int) details.stream().filter(d -> d.getIsCorrect() == 1).count());
            vo.setWrongCount((int) details.stream().filter(d -> d.getIsCorrect() == 0).count());
            
            vo.setDetails(details.stream().map(d -> {
                AnswerDetailVO detailVO = new AnswerDetailVO();
                BeanUtils.copyProperties(d, detailVO);
                return detailVO;
            }).collect(Collectors.toList()));
            
            return vo;
        }).collect(Collectors.toList());
    }
    
    public UserStatisticsVO getUserStatistics(Long userId) {
        UserStatisticsVO vo = new UserStatisticsVO();
        
        LambdaQueryWrapper<UserAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAnswer::getUserId, userId);
        List<UserAnswer> userAnswers = userAnswerMapper.selectList(wrapper);
        
        vo.setTotalAnswers((long) userAnswers.size());
        
        long totalCorrect = 0;
        long totalWrong = 0;
        long totalScore = 0;
        
        for (UserAnswer userAnswer : userAnswers) {
            List<AnswerDetail> details = answerDetailMapper.selectByAnswerId(userAnswer.getAnswerId());
            totalCorrect += details.stream().filter(d -> d.getIsCorrect() == 1).count();
            totalWrong += details.stream().filter(d -> d.getIsCorrect() == 0).count();
            totalScore += userAnswer.getScore();
        }
        
        vo.setCorrectAnswers(totalCorrect);
        vo.setWrongAnswers(totalWrong);
        vo.setTotalScore(totalScore);
        
        if (totalCorrect + totalWrong > 0) {
            vo.setAccuracyRate(Math.round(totalCorrect * 10000.0 / (totalCorrect + totalWrong)) / 100.0);
        } else {
            vo.setAccuracyRate(0.0);
        }
        
        vo.setStreakDays(calculateStreakDays(userId));
        vo.setTotalDays(calculateTotalDays(userId));
        
        return vo;
    }
    
    public List<AchievementVO> getUserAchievements(Long userId) {
        List<AchievementVO> achievements = new ArrayList<>();
        
        UserStatisticsVO stats = getUserStatistics(userId);
        
        achievements.add(createAchievement("first_answer", "初露锋芒", "完成第一次答题", "🎯", 
            stats.getTotalAnswers() >= 1, stats.getTotalAnswers().intValue(), 1));
        
        achievements.add(createAchievement("ten_answers", "勤学好问", "完成十次答题", "📚", 
            stats.getTotalAnswers() >= 10, stats.getTotalAnswers().intValue(), 10));
        
        achievements.add(createAchievement("hundred_answers", "学霸之路", "完成一百次答题", "🏆", 
            stats.getTotalAnswers() >= 100, stats.getTotalAnswers().intValue(), 100));
        
        achievements.add(createAchievement("perfect_score", "完美答卷", "获得满分", "⭐", 
            hasPerfectScore(userId), hasPerfectScore(userId) ? 1 : 0, 1));
        
        achievements.add(createAchievement("high_accuracy", "精准达人", "正确率达到90%以上", "🎖️", 
            stats.getAccuracyRate() >= 90, stats.getAccuracyRate().intValue(), 90));
        
        achievements.add(createAchievement("streak_7", "坚持不懈", "连续7天答题", "🔥", 
            stats.getStreakDays() >= 7, stats.getStreakDays(), 7));
        
        achievements.add(createAchievement("streak_30", "持之以恒", "连续30天答题", "💪", 
            stats.getStreakDays() >= 30, stats.getStreakDays(), 30));
        
        achievements.add(createAchievement("total_days_100", "百日征程", "累计答题100天", "📅", 
            stats.getTotalDays() >= 100, stats.getTotalDays(), 100));
        
        return achievements;
    }
    
    private AchievementVO createAchievement(String id, String name, String description, String icon, 
                                            Boolean unlocked, Integer progress, Integer target) {
        AchievementVO vo = new AchievementVO();
        vo.setId(id);
        vo.setName(name);
        vo.setDescription(description);
        vo.setIcon(icon);
        vo.setUnlocked(unlocked);
        vo.setProgress(progress);
        vo.setTarget(target);
        return vo;
    }
    
    private boolean hasPerfectScore(Long userId) {
        LambdaQueryWrapper<UserAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAnswer::getUserId, userId);
        List<UserAnswer> userAnswers = userAnswerMapper.selectList(wrapper);
        
        for (UserAnswer userAnswer : userAnswers) {
            List<AnswerDetail> details = answerDetailMapper.selectByAnswerId(userAnswer.getAnswerId());
            if (details.stream().allMatch(d -> d.getIsCorrect() == 1)) {
                return true;
            }
        }
        return false;
    }
    
    private int calculateStreakDays(Long userId) {
        LambdaQueryWrapper<UserAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAnswer::getUserId, userId);
        wrapper.orderByDesc(UserAnswer::getCreateTime);
        List<UserAnswer> userAnswers = userAnswerMapper.selectList(wrapper);
        
        if (userAnswers.isEmpty()) {
            return 0;
        }
        
        int streak = 0;
        LocalDate today = LocalDate.now();
        LocalDate expectedDate = today;
        
        for (UserAnswer userAnswer : userAnswers) {
            LocalDate answerDate = userAnswer.getCreateTime().toLocalDate();
            
            if (answerDate.equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (answerDate.isBefore(expectedDate)) {
                break;
            }
        }
        
        return streak;
    }
    
    private int calculateTotalDays(Long userId) {
        LambdaQueryWrapper<UserAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAnswer::getUserId, userId);
        List<UserAnswer> userAnswers = userAnswerMapper.selectList(wrapper);
        
        return (int) userAnswers.stream()
            .map(a -> a.getCreateTime().toLocalDate())
            .distinct()
            .count();
    }
}
