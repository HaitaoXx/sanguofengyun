package com.zwj.service;

import com.zwj.dao.*;
import com.zwj.entity.*;
import com.zwj.service.impl.StatisticsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StatisticsServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private ClubMapper clubMapper;
    @Mock
    private ActivityMapper activityMapper;
    @Mock
    private MemberMapper memberMapper;
    @Mock
    private ActivityApplyMapper activityApplyMapper;
    @Mock
    private MemberApplyMapper memberApplyMapper;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserCount() {
        User u1 = new User();
        User u2 = new User();
        when(userMapper.findAll()).thenReturn(Arrays.asList(u1, u2));

        int count = statisticsService.getUserCount();

        assertEquals(2, count);
        verify(userMapper).findAll();
    }

    @Test
    public void testGetUserCountNull() {
        when(userMapper.findAll()).thenReturn(null);

        int count = statisticsService.getUserCount();

        assertEquals(0, count);
        verify(userMapper).findAll();
    }

    @Test
    public void testGetClubCount() {
        when(clubMapper.findAll()).thenReturn(Arrays.asList(new Club()));

        int count = statisticsService.getClubCount();

        assertEquals(1, count);
        verify(clubMapper).findAll();
    }

    @Test
    public void testGetClubCountNull() {
        when(clubMapper.findAll()).thenReturn(null);

        int count = statisticsService.getClubCount();

        assertEquals(0, count);
        verify(clubMapper).findAll();
    }

    @Test
    public void testGetActivityCount() {
        when(activityMapper.findAll()).thenReturn(Arrays.asList(new Activity(), new Activity()));

        int count = statisticsService.getActivityCount();

        assertEquals(2, count);
        verify(activityMapper).findAll();
    }

    @Test
    public void testGetMemberCount() {
        when(memberMapper.findAll()).thenReturn(Arrays.asList(new Member()));

        int count = statisticsService.getMemberCount();

        assertEquals(1, count);
        verify(memberMapper).findAll();
    }

    @Test
    public void testGetActivityApplyCount() {
        when(activityApplyMapper.findAll()).thenReturn(Collections.emptyList());

        int count = statisticsService.getActivityApplyCount();

        assertEquals(0, count);
        verify(activityApplyMapper).findAll();
    }

    @Test
    public void testGetMemberApplyCount() {
        when(memberApplyMapper.findAll()).thenReturn(Arrays.asList(new MemberApply(), new MemberApply(), new MemberApply()));

        int count = statisticsService.getMemberApplyCount();

        assertEquals(3, count);
        verify(memberApplyMapper).findAll();
    }

    @Test
    public void testGetUserCountByRole() {
        when(userMapper.countByRole("admin")).thenReturn(1);
        when(userMapper.countByRole("leader")).thenReturn(2);
        when(userMapper.countByRole("student")).thenReturn(10);

        Map<String, Integer> result = statisticsService.getUserCountByRole();

        assertEquals(Integer.valueOf(1), result.get("admin"));
        assertEquals(Integer.valueOf(2), result.get("leader"));
        assertEquals(Integer.valueOf(10), result.get("student"));
        verify(userMapper).countByRole("admin");
        verify(userMapper).countByRole("leader");
        verify(userMapper).countByRole("student");
    }

    @Test
    public void testGetActivityCountByClub() {
        Map<String, Object> item1 = new HashMap<>();
        item1.put("club_name", "篮球社");
        item1.put("count", 5L);
        Map<String, Object> item2 = new HashMap<>();
        item2.put("club_name", null);
        item2.put("count", 3L);
        when(activityMapper.countByClub()).thenReturn(Arrays.asList(item1, item2));

        Map<String, Integer> result = statisticsService.getActivityCountByClub();

        assertEquals(Integer.valueOf(5), result.get("篮球社"));
        assertEquals(Integer.valueOf(3), result.get("未知"));
        verify(activityMapper).countByClub();
    }

    @Test
    public void testGetActivityCountByClubNullData() {
        when(activityMapper.countByClub()).thenReturn(null);

        Map<String, Integer> result = statisticsService.getActivityCountByClub();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(activityMapper).countByClub();
    }

    @Test
    public void testGetActivityCountByMonth() {
        Map<String, Object> item1 = new HashMap<>();
        item1.put("month", "2024-01");
        item1.put("count", 8L);
        when(activityMapper.countByMonth()).thenReturn(Arrays.asList(item1));

        Map<String, Integer> result = statisticsService.getActivityCountByMonth();

        assertEquals(Integer.valueOf(8), result.get("2024-01"));
        verify(activityMapper).countByMonth();
    }

    @Test
    public void testGetActivityCountByMonthNullData() {
        when(activityMapper.countByMonth()).thenReturn(null);

        Map<String, Integer> result = statisticsService.getActivityCountByMonth();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(activityMapper).countByMonth();
    }

    @Test
    public void testGetAllStatistics() {
        when(userMapper.findAll()).thenReturn(Arrays.asList(new User()));
        when(clubMapper.findAll()).thenReturn(Arrays.asList(new Club()));
        when(activityMapper.findAll()).thenReturn(Arrays.asList(new Activity()));
        when(memberMapper.findAll()).thenReturn(Arrays.asList(new Member()));
        when(activityApplyMapper.findAll()).thenReturn(Arrays.asList(new ActivityApply()));
        when(memberApplyMapper.findAll()).thenReturn(Arrays.asList(new MemberApply()));
        when(userMapper.countByRole(anyString())).thenReturn(0);
        when(activityMapper.countByClub()).thenReturn(Collections.emptyList());
        when(activityMapper.countByMonth()).thenReturn(Collections.emptyList());

        Map<String, Object> result = statisticsService.getAllStatistics();

        assertNotNull(result);
        assertEquals(1, result.get("userCount"));
        assertEquals(1, result.get("clubCount"));
        assertEquals(1, result.get("activityCount"));
        assertEquals(1, result.get("memberCount"));
        assertEquals(1, result.get("activityApplyCount"));
        assertEquals(1, result.get("memberApplyCount"));
        assertNotNull(result.get("userCountByRole"));
        assertNotNull(result.get("activityCountByClub"));
        assertNotNull(result.get("activityCountByMonth"));
    }
}
