package com.gxa.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class DifficultyAnalysisVO {
    private List<DifficultyData> difficultyDistribution;
    private List<QuestionDifficulty> questionDetails;
    
    @Data
    public static class DifficultyData {
        private String difficulty;
        private Long count;
        private Double avgAccuracy;
    }
    
    @Data
    public static class QuestionDifficulty {
        private Long questionId;
        private String questionText;
        private String questionType;
        private String difficulty;
        private Integer answerCount;
        private Integer correctCount;
        private Double accuracyRate;
    }
}