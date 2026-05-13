package com.zwj.service;

import com.zwj.entity.Activity;

import java.util.List;

public interface ActivityService {
    List<Activity> findAll();
    List<Activity> findByClubId(Integer clubId);
    Activity findById(Integer id);
    List<Activity> searchActivities(String keyword, Integer clubId);
    void save(Activity activity);
    void update(Activity activity);
    void delete(Integer id);
}