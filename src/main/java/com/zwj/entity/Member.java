package com.zwj.entity;

import java.util.Date;

public class Member {
    private Integer id;
    private Integer userId;
    private Integer clubId;
    private Date joinTime;
    private String role;
    
    public Member() {}
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getClubId() {
        return clubId;
    }
    
    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }
    
    public Date getJoinTime() {
        return joinTime;
    }
    
    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}