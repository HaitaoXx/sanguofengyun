package com.gxa.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxa.domain.dto.StatisticsQueryDTO;
import com.gxa.domain.entity.*;
import com.gxa.domain.vo.*;
import com.gxa.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatisticsService {
    
    @Autowired
    private StatisticsMapper statisticsMapper;
    
    @Autowired
    private UserAnswerMapper userAnswerMapper;
    
    @Autowired
    private AppMapper appMapper;
    
    @Autowired
    private AnswerDetailMapper answerDetailMapper;
    
    @Autowired
    private QuestionMapper questionMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    public StatisticsVO getStatistics(StatisticsQueryDTO dto) {
        LocalDate startDate = LocalDate.parse(dto.getStartDate(), DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(dto.getEndDate(), DateTimeFormatter.ISO_DATE);
        
        List<Statistics> statisticsList = statisticsMapper.selectByDateRange(dto.getAppId(), 
            startDate.format(DateTimeFormatter.ISO_DATE), 
            endDate.format(DateTimeFormatter.ISO_DATE));
        
        App app = appMapper.selectByAppId(dto.getAppId());
        
        List<UserAnswer> allAnswers = userAnswerMapper.selectList(new LambdaQueryWrapper<UserAnswer>()
            .eq(UserAnswer::getAppId, dto.getAppId()));
        
        StatisticsVO vo = new StatisticsVO();
        vo.setAppId(dto.getAppId());
        
        if (app != null) {
            vo.setViewCount(app.getViewCount() != null ? app.getViewCount() : 0);
            vo.setShareCount(app.getShareCount() != null ? app.getShareCount() : 0);
        } else {
            vo.setViewCount(0);
            vo.setShareCount(0);
        }
        
        vo.setAnswerCount(allAnswers.size());
        vo.setAvgScore(allAnswers.stream().mapToInt(UserAnswer::getScore).average().orElse(0.0));
        
        vo.setTrendData(buildTrendData(statisticsList));
        vo.setDimensionData(buildDimensionData(dto.getAppId()));
        vo.setOptionData(buildOptionData(dto.getAppId()));
        
        return vo;
    }
    
    @Transactional
    public void generateDailyStatistics(String appId) {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ISO_DATE);
        
        Statistics existing = statisticsMapper.selectOne(new LambdaQueryWrapper<Statistics>()
            .eq(Statistics::getAppId, appId)
            .eq(Statistics::getStatDate, today));
        
        List<UserAnswer> answers = userAnswerMapper.selectList(new LambdaQueryWrapper<UserAnswer>()
            .eq(UserAnswer::getAppId, appId)
            .ge(UserAnswer::getCreateTime, today.atStartOfDay())
            .lt(UserAnswer::getCreateTime, today.plusDays(1).atStartOfDay()));
        
        if (existing != null) {
            existing.setAnswerCount(answers.size());
            existing.setAvgScore(answers.stream().mapToInt(UserAnswer::getScore).average().orElse(0.0));
            statisticsMapper.updateById(existing);
        } else {
            Statistics statistics = new Statistics();
            statistics.setAppId(appId);
            statistics.setStatDate(today);
            statistics.setViewCount(0);
            statistics.setAnswerCount(answers.size());
            statistics.setShareCount(0);
            statistics.setAvgScore(answers.stream().mapToInt(UserAnswer::getScore).average().orElse(0.0));
            statistics.setStatJson("{}");
            
            statisticsMapper.insert(statistics);
        }
    }
    
    private List<Map<String, Object>> buildTrendData(List<Statistics> statisticsList) {
        return statisticsList.stream().map(s -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", s.getStatDate().format(DateTimeFormatter.ISO_DATE));
            map.put("viewCount", s.getViewCount());
            map.put("answerCount", s.getAnswerCount());
            map.put("shareCount", s.getShareCount());
            return map;
        }).collect(Collectors.toList());
    }
    
    private List<Map<String, Object>> buildDimensionData(String appId) {
        List<UserAnswer> answers = userAnswerMapper.selectList(new LambdaQueryWrapper<UserAnswer>()
            .eq(UserAnswer::getAppId, appId));
        
        Map<String, Integer> correctCount = new HashMap<>();
        Map<String, Integer> wrongCount = new HashMap<>();
        
        for (UserAnswer answer : answers) {
            List<AnswerDetail> details = answerDetailMapper.selectByAnswerId(answer.getAnswerId());
            for (AnswerDetail detail : details) {
                String questionType = getQuestionType(detail.getQuestionId());
                String key = questionType + "_" + (detail.getIsCorrect() == 1 ? "correct" : "wrong");
                
                if (detail.getIsCorrect() == 1) {
                    correctCount.merge(key, 1, Integer::sum);
                } else {
                    wrongCount.merge(key, 1, Integer::sum);
                }
            }
        }
        
        List<Map<String, Object>> data = new ArrayList<>();
        String[] types = {"single", "multiple", "judgment", "text"};
        String[] typeNames = {"单选题", "多选题", "判断题", "简答题"};
        
        for (int i = 0; i < types.length; i++) {
            String correctKey = types[i] + "_correct";
            String wrongKey = types[i] + "_wrong";
            int correct = correctCount.getOrDefault(correctKey, 0);
            int wrong = wrongCount.getOrDefault(wrongKey, 0);
            int total = correct + wrong;
            
            if (total > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", typeNames[i]);
                map.put("correct", correct);
                map.put("wrong", wrong);
                map.put("rate", Math.round(correct * 100.0 / total));
                data.add(map);
            }
        }
        
        return data;
    }
    
    private String getQuestionType(Long questionId) {
        if (questionId == null) return "single";
        Question question = questionMapper.selectById(questionId);
        return question != null && question.getQuestionType() != null ? question.getQuestionType() : "single";
    }
    
    private List<Map<String, Object>> buildOptionData(String appId) {
        List<UserAnswer> answers = userAnswerMapper.selectList(new LambdaQueryWrapper<UserAnswer>()
            .eq(UserAnswer::getAppId, appId));
        
        Map<String, Integer> optionCount = new HashMap<>();
        Map<String, Integer> optionCorrectCount = new HashMap<>();
        
        for (UserAnswer answer : answers) {
            List<AnswerDetail> details = answerDetailMapper.selectByAnswerId(answer.getAnswerId());
            for (AnswerDetail detail : details) {
                String userAnswer = detail.getUserAnswer();
                String correctAnswer = detail.getCorrectAnswer();
                
                if (userAnswer != null) {
                    String[] userOptions = userAnswer.toUpperCase().replaceAll("[,\\s]", "").split("");
                    for (String opt : userOptions) {
                        if (!opt.isEmpty()) {
                            String key = "选项" + opt;
                            optionCount.merge(key, 1, Integer::sum);
                            
                            if (correctAnswer != null && correctAnswer.toUpperCase().contains(opt)) {
                                optionCorrectCount.merge(key, 1, Integer::sum);
                            }
                        }
                    }
                }
            }
        }
        
        List<Map<String, Object>> data = new ArrayList<>();
        String[] options = {"A", "B", "C", "D", "E", "F"};
        
        for (String opt : options) {
            String key = "选项" + opt;
            int count = optionCount.getOrDefault(key, 0);
            int correct = optionCorrectCount.getOrDefault(key, 0);
            
            if (count > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", key);
                map.put("value", count);
                map.put("correct", correct);
                map.put("rate", Math.round(correct * 100.0 / count));
                data.add(map);
            }
        }
        
        return data;
    }
    
    public BehaviorAnalysisVO getBehaviorAnalysis(String appId) {
        BehaviorAnalysisVO vo = new BehaviorAnalysisVO();
        
        List<UserAnswer> allAnswers = userAnswerMapper.selectList(new LambdaQueryWrapper<UserAnswer>()
            .eq(UserAnswer::getAppId, appId));
        
        Set<Long> userIds = allAnswers.stream().map(UserAnswer::getUserId).collect(Collectors.toSet());
        vo.setTotalUsers((long) userIds.size());
        
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        Set<Long> activeUserIds = allAnswers.stream()
            .filter(a -> a.getCreateTime().toLocalDate().isAfter(thirtyDaysAgo))
            .map(UserAnswer::getUserId)
            .collect(Collectors.toSet());
        vo.setActiveUsers((long) activeUserIds.size());
        
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        Set<Long> newUserIds = userMapper.selectList(new LambdaQueryWrapper<User>()
            .ge(User::getCreateTime, sevenDaysAgo.atStartOfDay()))
            .stream()
            .map(User::getId)
            .collect(Collectors.toSet());
        vo.setNewUsers((long) newUserIds.size());
        
        double avgDuration = allAnswers.stream()
            .filter(a -> a.getDuration() != null && a.getDuration() > 0)
            .mapToDouble(UserAnswer::getDuration)
            .average()
            .orElse(0.0);
        vo.setAvgAnswerTime(Math.round(avgDuration * 100) / 100.0);
        
        double avgQuestions = allAnswers.stream()
            .mapToInt(a -> {
                List<AnswerDetail> details = answerDetailMapper.selectByAnswerId(a.getAnswerId());
                return details.size();
            })
            .average()
            .orElse(0.0);
        vo.setAvgQuestionsPerSession(Math.round(avgQuestions * 100) / 100.0);
        
        vo.setHourlyDistribution(buildHourlyDistribution(allAnswers));
        vo.setWeeklyDistribution(buildWeeklyDistribution(allAnswers));
        
        return vo;
    }
    
    private List<BehaviorAnalysisVO.HourlyData> buildHourlyDistribution(List<UserAnswer> answers) {
        Map<Integer, Long> hourlyMap = answers.stream()
            .collect(Collectors.groupingBy(
                a -> a.getCreateTime().getHour(),
                Collectors.counting()
            ));
        
        List<BehaviorAnalysisVO.HourlyData> hourlyData = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            BehaviorAnalysisVO.HourlyData data = new BehaviorAnalysisVO.HourlyData();
            data.setHour(hour);
            data.setAnswerCount(hourlyMap.getOrDefault(hour, 0L));
            hourlyData.add(data);
        }
        return hourlyData;
    }
    
    private List<BehaviorAnalysisVO.WeeklyData> buildWeeklyDistribution(List<UserAnswer> answers) {
        Map<String, Long> weeklyMap = answers.stream()
            .collect(Collectors.groupingBy(
                a -> {
                    int dayOfWeek = a.getCreateTime().getDayOfWeek().getValue();
                    String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
                    return days[dayOfWeek - 1];
                },
                Collectors.counting()
            ));
        
        List<BehaviorAnalysisVO.WeeklyData> weeklyData = new ArrayList<>();
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for (String day : days) {
            BehaviorAnalysisVO.WeeklyData data = new BehaviorAnalysisVO.WeeklyData();
            data.setDayOfWeek(day);
            data.setAnswerCount(weeklyMap.getOrDefault(day, 0L));
            weeklyData.add(data);
        }
        return weeklyData;
    }
    
    public DifficultyAnalysisVO getDifficultyAnalysis(String appId) {
        DifficultyAnalysisVO vo = new DifficultyAnalysisVO();
        
        List<Question> questions = questionMapper.selectByAppId(appId);
        List<DifficultyAnalysisVO.DifficultyData> difficultyDistribution = new ArrayList<>();
        List<DifficultyAnalysisVO.QuestionDifficulty> questionDetails = new ArrayList<>();
        
        Map<String, List<Question>> difficultyGroups = questions.stream()
            .collect(Collectors.groupingBy(q -> {
                Integer score = q.getScore();
                if (score != null) {
                    if (score <= 5) return "简单";
                    if (score <= 10) return "中等";
                    return "困难";
                }
                return "中等";
            }));
        
        for (Map.Entry<String, List<Question>> entry : difficultyGroups.entrySet()) {
            DifficultyAnalysisVO.DifficultyData data = new DifficultyAnalysisVO.DifficultyData();
            data.setDifficulty(entry.getKey());
            data.setCount((long) entry.getValue().size());
            
            double totalAccuracy = 0;
            int count = 0;
            
            for (Question question : entry.getValue()) {
                double accuracy = calculateQuestionAccuracy(question.getId(), appId);
                totalAccuracy += accuracy;
                count++;
                
                DifficultyAnalysisVO.QuestionDifficulty qd = new DifficultyAnalysisVO.QuestionDifficulty();
                qd.setQuestionId(question.getId());
                qd.setQuestionText(truncateText(question.getQuestionText(), 50));
                qd.setQuestionType(question.getQuestionType());
                qd.setDifficulty(entry.getKey());
                qd.setAccuracyRate(accuracy);
                questionDetails.add(qd);
            }
            
            data.setAvgAccuracy(Math.round(totalAccuracy / count * 100) / 100.0);
            difficultyDistribution.add(data);
        }
        
        vo.setDifficultyDistribution(difficultyDistribution);
        vo.setQuestionDetails(questionDetails);
        return vo;
    }
    
    private double calculateQuestionAccuracy(Long questionId, String appId) {
        List<AnswerDetail> details = answerDetailMapper.selectList(new LambdaQueryWrapper<AnswerDetail>()
            .eq(AnswerDetail::getQuestionId, questionId)
            .eq(AnswerDetail::getAppId, appId));
        
        if (details.isEmpty()) return 0.0;
        
        long correctCount = details.stream().filter(d -> d.getIsCorrect() == 1).count();
        return Math.round(correctCount * 10000.0 / details.size()) / 100.0;
    }
    
    private String truncateText(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }
    
    public KnowledgeAnalysisVO getKnowledgeAnalysis(String appId) {
        KnowledgeAnalysisVO vo = new KnowledgeAnalysisVO();
        
        List<Question> questions = questionMapper.selectByAppId(appId);
        
        Map<String, List<Question>> knowledgeGroups = questions.stream()
            .collect(Collectors.groupingBy(q -> extractKnowledge(q.getConfigJson())));
        
        List<KnowledgeAnalysisVO.KnowledgeData> knowledgeDistribution = new ArrayList<>();
        List<KnowledgeAnalysisVO.WeakPoint> weakPoints = new ArrayList<>();
        List<KnowledgeAnalysisVO.StrongPoint> strongPoints = new ArrayList<>();
        
        for (Map.Entry<String, List<Question>> entry : knowledgeGroups.entrySet()) {
            KnowledgeAnalysisVO.KnowledgeData data = new KnowledgeAnalysisVO.KnowledgeData();
            data.setKnowledge(entry.getKey());
            data.setQuestionCount((long) entry.getValue().size());
            
            long totalAnswerCount = 0;
            long totalCorrectCount = 0;
            
            for (Question question : entry.getValue()) {
                List<AnswerDetail> details = answerDetailMapper.selectList(new LambdaQueryWrapper<AnswerDetail>()
                    .eq(AnswerDetail::getQuestionId, question.getId())
                    .eq(AnswerDetail::getAppId, appId));
                
                totalAnswerCount += details.size();
                totalCorrectCount += details.stream().filter(d -> d.getIsCorrect() == 1).count();
            }
            
            data.setAnswerCount(totalAnswerCount);
            data.setCorrectCount(totalCorrectCount);
            data.setAccuracyRate(totalAnswerCount > 0 
                ? Math.round(totalCorrectCount * 10000.0 / totalAnswerCount) / 100.0 
                : 0.0);
            
            knowledgeDistribution.add(data);
            
            if (data.getAccuracyRate() < 60 && data.getAnswerCount() >= 5) {
                KnowledgeAnalysisVO.WeakPoint wp = new KnowledgeAnalysisVO.WeakPoint();
                wp.setKnowledge(data.getKnowledge());
                wp.setAccuracyRate(data.getAccuracyRate());
                wp.setQuestionCount(data.getQuestionCount());
                weakPoints.add(wp);
            }
            
            if (data.getAccuracyRate() >= 85 && data.getAnswerCount() >= 5) {
                KnowledgeAnalysisVO.StrongPoint sp = new KnowledgeAnalysisVO.StrongPoint();
                sp.setKnowledge(data.getKnowledge());
                sp.setAccuracyRate(data.getAccuracyRate());
                sp.setQuestionCount(data.getQuestionCount());
                strongPoints.add(sp);
            }
        }
        
        weakPoints.sort(Comparator.comparing(KnowledgeAnalysisVO.WeakPoint::getAccuracyRate));
        strongPoints.sort(Comparator.comparing(KnowledgeAnalysisVO.StrongPoint::getAccuracyRate).reversed());
        
        vo.setKnowledgeDistribution(knowledgeDistribution);
        vo.setWeakPoints(weakPoints.subList(0, Math.min(5, weakPoints.size())));
        vo.setStrongPoints(strongPoints.subList(0, Math.min(5, strongPoints.size())));
        
        return vo;
    }
    
    private String extractKnowledge(String configJson) {
        if (configJson == null || configJson.isEmpty()) {
            return "未分类";
        }
        
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> config = mapper.readValue(configJson, Map.class);
            return (String) config.getOrDefault("knowledge", "未分类");
        } catch (Exception e) {
            log.error("解析配置JSON失败", e);
            return "未分类";
        }
    }
}
