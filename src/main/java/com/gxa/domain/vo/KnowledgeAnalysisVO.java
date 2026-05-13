package com.gxa.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class KnowledgeAnalysisVO {
    private List<KnowledgeData> knowledgeDistribution;
    private List<WeakPoint> weakPoints;
    private List<StrongPoint> strongPoints;
    
    @Data
    public static class KnowledgeData {
        private String knowledge;
        private Long questionCount;
        private Long answerCount;
        private Long correctCount;
        private Double accuracyRate;
    }
    
    @Data
    public static class WeakPoint {
        private String knowledge;
        private Double accuracyRate;
        private Long questionCount;
    }
    
    @Data
    public static class StrongPoint {
        private String knowledge;
        private Double accuracyRate;
        private Long questionCount;
    }
}