package com.zwj.entity;

public class MemberApply {
    private Integer id;
    private Integer clubId;
    private Integer userId;
    private String status;
    private String applyTime;
    
    public MemberApply() {}
    
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
