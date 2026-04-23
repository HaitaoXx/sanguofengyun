package com.zwj.entity;

import java.util.Date;

public class Activity {
    private Integer id;
    private Integer clubId;
    private String title;
    private String content;
    private Date activityTime;
    private Double duration;
    private String location;
    private String createTime;
    
    public Activity() {}
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getClubId() {
        return clubId;
    }
    
    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Date getActivityTime() {
        return activityTime;
    }
    
    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }
    
    public Double getDuration() {
        return duration;
    }
    
    public void setDuration(Double duration) {
        this.duration = duration;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    public String getName() {
        return title;
    }
    
    public void setName(String name) {
        this.title = name;
    }
    
    public String getDescription() {
        return content;
    }
    
    public void setDescription(String description) {
        this.content = description;
    }
}