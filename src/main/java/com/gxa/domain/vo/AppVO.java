package com.gxa.domain.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppVO {
    private Long id;
    private String appId;
    private Long userId;
    private String username;
    private String nickname;
    private String title;
    private String description;
    private String type;
    private String coverImage;
    private String configJson;
    private Integer status;
    private Integer viewCount;
    private Integer answerCount;
    private Integer shareCount;
    private Integer isAiGenerated;
    private List<QuestionVO> questions;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
