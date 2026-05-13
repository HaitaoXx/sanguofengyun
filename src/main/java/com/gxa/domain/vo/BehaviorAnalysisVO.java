package com.gxa.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BehaviorAnalysisVO {
    private Long totalUsers;
    private Long activeUsers;
    private Long newUsers;
    private Double avgAnswerTime;
    private Double avgQuestionsPerSession;
    private List<HourlyData> hourlyDistribution;
    private List<WeeklyData> weeklyDistribution;
    
    @Data
    public static class HourlyData {
        private Integer hour;
        private Long answerCount;
    }
    
    @Data
    public static class WeeklyData {
        private String dayOfWeek;
        private Long answerCount;
    }
}