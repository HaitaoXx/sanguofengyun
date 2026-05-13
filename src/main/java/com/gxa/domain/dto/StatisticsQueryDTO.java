package com.gxa.domain.dto;

import lombok.Data;

@Data
public class StatisticsQueryDTO {
    private String appId;
    private String startDate;
    private String endDate;
}
