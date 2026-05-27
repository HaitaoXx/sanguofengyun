package com.zwj.service.impl;

import com.zwj.dao.*;
import com.zwj.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ClubMapper clubMapper;
    
    @Autowired
    private ActivityMapper activityMapper;
    
    @Autowired
    private MemberMapper memberMapper;
    
    @Autowired
    private ActivityApplyMapper activityApplyMapper;
    
    @Autowired
    private MemberApplyMapper memberApplyMapper;

    @Override
    public int getUserCount() {
        List<?> users = userMapper.findAll();
        return users != null ? users.size() : 0;
    }

    @Override
    public int getClubCount() {
        List<?> clubs = clubMapper.findAll();
        return clubs != null ? clubs.size() : 0;
    }

    @Override
    public int getActivityCount() {
        List<?> activities = activityMapper.findAll();
        return activities != null ? activities.size() : 0;
    }

    @Override
    public int getMemberCount() {
        List<?> members = memberMapper.findAll();
        return members != null ? members.size() : 0;
    }

    @Override
    public int getActivityApplyCount() {
        List<?> applies = activityApplyMapper.findAll();
        return applies != null ? applies.size() : 0;
    }

    @Override
    public int getMemberApplyCount() {
        List<?> applies = memberApplyMapper.findAll();
        return applies != null ? applies.size() : 0;
    }

    @Override
    public Map<String, Integer> getUserCountByRole() {
        Map<String, Integer> result = new HashMap<>();
        result.put("admin", userMapper.countByRole("admin"));
        result.put("leader", userMapper.countByRole("leader"));
        result.put("student", userMapper.countByRole("student"));
        return result;
    }

    @Override
    public Map<String, Integer> getActivityCountByClub() {
        Map<String, Integer> result = new HashMap<>();
        List<Map<String, Object>> data = activityMapper.countByClub();
        if (data != null) {
            for (Map<String, Object> item : data) {
                String clubName = (String) item.get("club_name");
                Long countLong = (Long) item.get("count");
                Integer count = countLong != null ? countLong.intValue() : 0;
                result.put(clubName != null ? clubName : "未知", count);
            }
        }
        return result;
    }

    @Override
    public Map<String, Integer> getActivityCountByMonth() {
        Map<String, Integer> result = new HashMap<>();
        List<Map<String, Object>> data = activityMapper.countByMonth();
        if (data != null) {
            for (Map<String, Object> item : data) {
                String month = (String) item.get("month");
                Long countLong = (Long) item.get("count");
                Integer count = countLong != null ? countLong.intValue() : 0;
                result.put(month != null ? month : "未知", count);
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> getAllStatistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("userCount", getUserCount());
        result.put("clubCount", getClubCount());
        result.put("activityCount", getActivityCount());
        result.put("memberCount", getMemberCount());
        result.put("activityApplyCount", getActivityApplyCount());
        result.put("memberApplyCount", getMemberApplyCount());
        result.put("userCountByRole", getUserCountByRole());
        result.put("activityCountByClub", getActivityCountByClub());
        result.put("activityCountByMonth", getActivityCountByMonth());
        return result;
    }
}
