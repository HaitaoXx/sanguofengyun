package com.gxa.domain.vo;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class StatisticsVO {
    private String appId;
    private String appTitle;
    private LocalDate statDate;
    private Integer viewCount;
    private Integer answerCount;
    private Integer shareCount;
    private Double avgScore;
    private String statJson;
    private List<Map<String, Object>> trendData;
    private List<Map<String, Object>> dimensionData;
    private List<Map<String, Object>> optionData;
}
