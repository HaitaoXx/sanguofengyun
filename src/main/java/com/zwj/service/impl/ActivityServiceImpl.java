package com.zwj.service.impl;

import com.zwj.dao.ActivityMapper;
import com.zwj.entity.Activity;
import com.zwj.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<Activity> findAll() {
        return activityMapper.findAll();
    }

    @Override
    public List<Activity> findByClubId(Integer clubId) {
        return activityMapper.findByClubId(clubId);
    }

    @Override
    public Activity findById(Integer id) {
        return activityMapper.findById(id);
    }

    @Override
    public List<Activity> searchActivities(String keyword, Integer clubId) {
        return activityMapper.searchActivities(keyword, clubId);
    }

    @Override
    public void save(Activity activity) {
        if (activity.getId() == null) {
            activityMapper.insert(activity);
        } else {
            activityMapper.update(activity);
        }
    }

    @Override
    public void update(Activity activity) {
        activityMapper.update(activity);
    }

    @Override
    public void delete(Integer id) {
        activityMapper.delete(id);
    }
}