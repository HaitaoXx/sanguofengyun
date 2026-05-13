package com.gxa.domain.dto;

import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String questionText;
    private String questionType;
    private String options;
    private Integer score;
    private Integer orderNum;
    private String configJson;
}
