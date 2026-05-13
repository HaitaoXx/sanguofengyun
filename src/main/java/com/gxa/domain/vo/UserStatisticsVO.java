package com.gxa.domain.vo;

import lombok.Data;

@Data
public class UserStatisticsVO {
    private Long totalAnswers;
    private Long correctAnswers;
    private Long wrongAnswers;
    private Double accuracyRate;
    private Long totalScore;
    private Long maxScore;
    private Integer streakDays;
    private Integer totalDays;
}