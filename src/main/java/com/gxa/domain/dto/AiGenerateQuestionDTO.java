package com.gxa.domain.dto;

import lombok.Data;

@Data
public class AiGenerateQuestionDTO {
    private String appId;
    private String topic;
    private Integer questionCount;
    private String questionType;
    private String difficulty;
}
