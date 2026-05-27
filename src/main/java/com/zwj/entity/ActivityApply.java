package com.zwj.entity;

public class ActivityApply {
    private Integer id;
    private Integer activityId;
    private Integer userId;
    private String status;
    private String applyTime;
    
    public ActivityApply() {}
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getActivityId() {
        return activityId;
    }
    
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getApplyTime() {
        return applyTime;
    }
    
    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }
}
