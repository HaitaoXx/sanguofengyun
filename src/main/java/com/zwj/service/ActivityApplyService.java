package com.zwj.service;

import com.zwj.entity.ActivityApply;

import java.util.List;

public interface ActivityApplyService {
    void apply(Integer activityId, Integer userId);
    void updateStatus(Integer id, String status);
    void delete(Integer id);
    ActivityApply findById(Integer id);
    List<ActivityApply> findByActivityId(Integer activityId);
    List<ActivityApply> findByUserId(Integer userId);
    boolean hasApplied(Integer activityId, Integer userId);
}
