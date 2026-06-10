package com.zwj.service.impl;

import com.zwj.dao.ActivityApplyMapper;
import com.zwj.entity.ActivityApply;
import com.zwj.service.ActivityApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityApplyServiceImpl implements ActivityApplyService {
    @Autowired
    private ActivityApplyMapper activityApplyMapper;

    @Override
    @Transactional
    public void apply(Integer activityId, Integer userId) {
        ActivityApply apply = new ActivityApply();
        apply.setActivityId(activityId);
        apply.setUserId(userId);
        apply.setStatus("pending");
        activityApplyMapper.insert(apply);
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, String status) {
        ActivityApply apply = activityApplyMapper.findById(id);
        if (apply != null) {
            apply.setStatus(status);
            activityApplyMapper.update(apply);
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        activityApplyMapper.delete(id);
    }

    @Override
    public ActivityApply findById(Integer id) {
        return activityApplyMapper.findById(id);
    }

    @Override
    public List<ActivityApply> findByActivityId(Integer activityId) {
        return activityApplyMapper.findByActivityId(activityId);
    }

    @Override
    public List<ActivityApply> findByUserId(Integer userId) {
        return activityApplyMapper.findByUserId(userId);
    }

    @Override
    public boolean hasApplied(Integer activityId, Integer userId) {
        ActivityApply apply = activityApplyMapper.findByActivityIdAndUserId(activityId, userId);
        return apply != null;
    }
}
