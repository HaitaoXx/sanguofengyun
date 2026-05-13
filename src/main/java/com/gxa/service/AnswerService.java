package com.gxa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxa.domain.dto.SubmitAnswerDTO;
import com.gxa.domain.entity.AnswerDetail;
import com.gxa.domain.entity.App;
import com.gxa.domain.entity.Question;
import com.gxa.domain.entity.UserAnswer;
import com.gxa.domain.vo.AnswerDetailVO;
import com.gxa.domain.vo.AnswerResultVO;
import com.gxa.mapper.AnswerDetailMapper;
import com.gxa.mapper.AppMapper;
import com.gxa.mapper.QuestionMapper;
import com.gxa.mapper.UserAnswerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnswerService {
    
    @Autowired
    private UserAnswerMapper userAnswerMapper;
    
    @Autowired
    private AnswerDetailMapper answerDetailMapper;
    
    @Autowired
    private AppMapper appMapper;
    
    @Autowired
    private QuestionMapper questionMapper;
    
    @Autowired
    private AppService appService;
    
    @Autowired
    private AiService aiService;
    
    @Autowired
    private StatisticsService statisticsService;
    
    @Transactional
    public AnswerResultVO submitAnswer(SubmitAnswerDTO dto, Long userId, String ipAddress) {
        App app = appMapper.selectByAppId(dto.getAppId());
        if (app == null) {
            throw new RuntimeException("应用不存在");
        }
        
        if (app.getStatus() != 1) {
            throw new RuntimeException("应用未通过审核，无法答题");
        }
        
        String answerId = generateAnswerId();
        
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(dto.getAppId());
        userAnswer.setUserId(userId);
        userAnswer.setAnswerId(answerId);
        userAnswer.setScore(0);
        userAnswer.setDuration(dto.getDuration());
        userAnswer.setIpAddress(ipAddress);
        userAnswerMapper.insert(userAnswer);
        
        List<Question> questions = questionMapper.selectByAppId(dto.getAppId());
        int totalScore = 0;
        
        for (SubmitAnswerDTO.AnswerItemDTO item : dto.getAnswers()) {
            Question question = questions.stream()
                .filter(q -> q.getId().equals(item.getQuestionId()))
                .findFirst()
                .orElse(null);
            
            if (question != null) {
                AnswerDetail detail = new AnswerDetail();
                detail.setAppId(dto.getAppId());
                detail.setAnswerId(answerId);
                detail.setQuestionId(item.getQuestionId());
                detail.setUserAnswer(item.getUserAnswer());
                detail.setCorrectAnswer(getCorrectAnswer(question));
                detail.setScore(calculateScore(question, item.getUserAnswer()));
                detail.setIsCorrect(checkCorrect(question, item.getUserAnswer()) ? 1 : 0);
                answerDetailMapper.insert(detail);
                
                totalScore += detail.getScore();
            }
        }
        
        userAnswer.setScore(totalScore);
        userAnswerMapper.updateById(userAnswer);
        
        appService.incrementAnswerCount(dto.getAppId());
        
        statisticsService.generateDailyStatistics(dto.getAppId());
        
        List<AnswerDetail> details = answerDetailMapper.selectByAnswerId(answerId);
        
        aiService.generateAiCommentAsync(dto.getAppId(), answerId, 
            details.stream().map(this::convertDetailToVO).collect(Collectors.toList()));
        
        return buildAnswerResultVO(userAnswer, app, details);
    }
    
    public AnswerResultVO getAnswerResult(String answerId) {
        UserAnswer userAnswer = userAnswerMapper.selectByAnswerId(answerId);
        if (userAnswer == null) {
            throw new RuntimeException("答题记录不存在");
        }
        
        App app = appMapper.selectByAppId(userAnswer.getAppId());
        List<AnswerDetail> details = answerDetailMapper.selectByAnswerId(answerId);
        
        return buildAnswerResultVO(userAnswer, app, details);
    }
    
    public List<AnswerResultVO> getHistoryAnswers(String appId, String keyword) {
        LambdaQueryWrapper<UserAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAnswer::getAppId, appId);
        wrapper.orderByDesc(UserAnswer::getCreateTime);
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(UserAnswer::getAnswerId, keyword));
            
            try {
                Long userId = Long.parseLong(keyword.trim());
                wrapper.or().eq(UserAnswer::getUserId, userId);
            } catch (NumberFormatException e) {
                log.debug("关键词不是有效的用户ID，仅搜索答题ID");
            }
        }
        
        List<UserAnswer> userAnswers = userAnswerMapper.selectList(wrapper);
        
        return userAnswers.stream().map(userAnswer -> {
            App app = appMapper.selectByAppId(userAnswer.getAppId());
            List<AnswerDetail> details = answerDetailMapper.selectByAnswerId(userAnswer.getAnswerId());
            return buildAnswerResultVO(userAnswer, app, details);
        }).collect(Collectors.toList());
    }
    
    private AnswerResultVO buildAnswerResultVO(UserAnswer userAnswer, App app, List<AnswerDetail> details) {
        AnswerResultVO vo = new AnswerResultVO();
        vo.setAnswerId(userAnswer.getAnswerId());
        vo.setAppId(userAnswer.getAppId());
        vo.setAppTitle(app.getTitle());
        vo.setUserId(userAnswer.getUserId());
        vo.setScore(userAnswer.getScore());
        vo.setResultJson(userAnswer.getResultJson());
        vo.setAiComment(userAnswer.getAiComment());
        vo.setDuration(userAnswer.getDuration());
        vo.setCreateTime(userAnswer.getCreateTime());
        vo.setDetails(details.stream().map(this::convertDetailToVO).collect(Collectors.toList()));
        return vo;
    }
    
    private AnswerDetailVO convertDetailToVO(AnswerDetail detail) {
        AnswerDetailVO vo = new AnswerDetailVO();
        vo.setQuestionId(detail.getQuestionId());
        vo.setUserAnswer(detail.getUserAnswer());
        vo.setCorrectAnswer(detail.getCorrectAnswer());
        vo.setScore(detail.getScore());
        vo.setIsCorrect(detail.getIsCorrect());
        
        Question question = questionMapper.selectById(detail.getQuestionId());
        if (question != null) {
            vo.setQuestionText(question.getQuestionText());
            vo.setOptions(question.getOptions());
        }
        
        return vo;
    }
    
    private String getCorrectAnswer(Question question) {
        String correctAnswer = question.getCorrectAnswer();
        if (correctAnswer != null && !correctAnswer.trim().isEmpty()) {
            return correctAnswer;
        }
        
        if ("single".equals(question.getQuestionType()) || "multiple".equals(question.getQuestionType())) {
            try {
                if (question.getOptions() != null && !question.getOptions().isEmpty()) {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    String[] options = mapper.readValue(question.getOptions(), String[].class);
                    if (options.length > 0) {
                        return "A";
                    }
                }
            } catch (Exception e) {
                log.error("获取正确答案失败", e);
            }
        }
        
        return "A";
    }
    
    private Integer calculateScore(Question question, String userAnswer) {
        if (checkCorrect(question, userAnswer)) {
            return question.getScore();
        }
        return 0;
    }
    
    private boolean checkCorrect(Question question, String userAnswer) {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            return false;
        }
        
        String correctAnswer = question.getCorrectAnswer();
        if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
            return false;
        }
        
        try {
            if ("single".equals(question.getQuestionType())) {
                return correctAnswer.trim().equalsIgnoreCase(userAnswer.trim());
            } else if ("multiple".equals(question.getQuestionType())) {
                // 处理多选题答案比较，去除分隔符并排序后比较
                String processedCorrect = correctAnswer.trim().toUpperCase().replaceAll("[,\s]", "");
                String processedUser = userAnswer.trim().toUpperCase().replaceAll("[,\s]", "");
                
                // 转换为字符数组并排序
                char[] correctChars = processedCorrect.toCharArray();
                char[] userChars = processedUser.toCharArray();
                
                if (correctChars.length != userChars.length) {
                    return false;
                }
                
                java.util.Arrays.sort(correctChars);
                java.util.Arrays.sort(userChars);
                
                return new String(correctChars).equals(new String(userChars));
            } else if ("text".equals(question.getQuestionType())) {
                return userAnswer.trim().length() > 10;
            } else if ("judgment".equals(question.getQuestionType())) {
                // 处理判断题，统一比较格式
                return correctAnswer.trim().equalsIgnoreCase(userAnswer.trim());
            }
        } catch (Exception e) {
            log.error("检查答案正确性失败", e);
        }
        
        return false;
    }
    
    private String generateAnswerId() {
        return "ANS" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
