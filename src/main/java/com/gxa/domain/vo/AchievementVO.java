package com.gxa.domain.vo;

import lombok.Data;

@Data
public class AchievementVO {
    private String id;
    private String name;
    private String description;
    private String icon;
    private Boolean unlocked;
    private Integer progress;
    private Integer target;
}