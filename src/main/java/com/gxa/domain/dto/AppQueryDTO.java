package com.gxa.domain.dto;

import lombok.Data;

@Data
public class AppQueryDTO {
    private String type;
    private Integer status;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
}
