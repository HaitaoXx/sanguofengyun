package com.zwj.service;

import java.util.Map;

public interface StatisticsService {
    int getUserCount();
    int getClubCount();
    int getActivityCount();
    int getMemberCount();
    int getActivityApplyCount();
    int getMemberApplyCount();
    Map<String, Integer> getUserCountByRole();
    Map<String, Integer> getActivityCountByClub();
    Map<String, Integer> getActivityCountByMonth();
    Map<String, Object> getAllStatistics();
}
