package com.gxa.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuestionVO {
    private Long id;
    private String appId;
    private String questionText;
    private String questionType;
    private String options;
    private String correctAnswer;
    private Integer score;
    private Integer orderNum;
    private String configJson;
    private LocalDateTime createTime;
}
