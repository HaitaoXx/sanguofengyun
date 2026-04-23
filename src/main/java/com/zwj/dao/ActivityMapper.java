package com.zwj.dao;

import com.zwj.entity.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityMapper {
    List<Activity> findAll();
    List<Activity> findByClubId(@Param("clubId") Integer clubId);
    Activity findById(Integer id);
    List<Activity> searchActivities(@Param("keyword") String keyword, @Param("clubId") Integer clubId);
    void insert(Activity activity);
    void update(Activity activity);
    void delete(Integer id);
}