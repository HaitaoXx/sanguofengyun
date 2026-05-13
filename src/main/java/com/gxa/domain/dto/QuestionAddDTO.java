package com.gxa.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionAddDTO {
    private String appId;
    private String questionText;
    private String questionType;
    private List<String> options;
    private String correctAnswer;
    private Integer score;
    private Integer orderNum;
}