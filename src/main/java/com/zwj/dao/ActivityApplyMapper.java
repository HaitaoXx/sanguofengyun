package com.zwj.dao;

import com.zwj.entity.ActivityApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityApplyMapper {
    void insert(ActivityApply apply);
    void update(ActivityApply apply);
    void delete(Integer id);
    ActivityApply findById(Integer id);
    List<ActivityApply> findByActivityId(Integer activityId);
    List<ActivityApply> findByUserId(Integer userId);
    ActivityApply findByActivityIdAndUserId(@Param("activityId") Integer activityId, @Param("userId") Integer userId);
    List<ActivityApply> findAll();
    int count();
}
