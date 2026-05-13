package com.gxa.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnswerResultVO {
    private String answerId;
    private String appId;
    private String appTitle;
    private Long userId;
    private Integer score;
    private String resultJson;
    private String aiComment;
    private Integer duration;
    private List<AnswerDetailVO> details;
    private LocalDateTime createTime;
}
