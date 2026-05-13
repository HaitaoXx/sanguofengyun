package com.gxa.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserAnswerHistoryVO {
    private String answerId;
    private String appId;
    private String appName;
    private Integer totalQuestions;
    private Integer correctCount;
    private Integer wrongCount;
    private Integer score;
    private LocalDateTime submitTime;
    private List<AnswerDetailVO> details;
}