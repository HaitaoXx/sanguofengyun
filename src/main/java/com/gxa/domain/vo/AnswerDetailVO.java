package com.gxa.domain.vo;

import lombok.Data;

@Data
public class AnswerDetailVO {
    private Long questionId;
    private String questionText;
    private String options;
    private String userAnswer;
    private String correctAnswer;
    private Integer score;
    private Integer isCorrect;
}
