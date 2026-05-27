package com.zwj.service;

import com.zwj.dao.ActivityMapper;
import com.zwj.entity.Activity;
import com.zwj.service.impl.ActivityServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ActivityServiceTest {

    @Mock
    private ActivityMapper activityMapper;

    @InjectMocks
    private ActivityServiceImpl activityService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Activity a1 = new Activity();
        a1.setId(1);
        a1.setTitle("活动1");
        when(activityMapper.findAll()).thenReturn(Arrays.asList(a1));

        List<Activity> result = activityService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("活动1", result.get(0).getTitle());
        verify(activityMapper).findAll();
    }

    @Test
    public void testFindAllEmpty() {
        when(activityMapper.findAll()).thenReturn(Collections.emptyList());

        List<Activity> result = activityService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(activityMapper).findAll();
    }

    @Test
    public void testFindByClubId() {
        Activity a1 = new Activity();
        a1.setId(1);
        when(activityMapper.findByClubId(10)).thenReturn(Arrays.asList(a1));

        List<Activity> result = activityService.findByClubId(10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityMapper).findByClubId(10);
    }

    @Test
    public void testFindByClubIdEmpty() {
        when(activityMapper.findByClubId(999)).thenReturn(Collections.emptyList());

        List<Activity> result = activityService.findByClubId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(activityMapper).findByClubId(999);
    }

    @Test
    public void testFindById() {
        Activity activity = new Activity();
        activity.setId(1);
        activity.setTitle("测试活动");
        when(activityMapper.findById(1)).thenReturn(activity);

        Activity result = activityService.findById(1);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getId());
        assertEquals("测试活动", result.getTitle());
        verify(activityMapper).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(activityMapper.findById(999)).thenReturn(null);

        Activity result = activityService.findById(999);

        assertNull(result);
        verify(activityMapper).findById(999);
    }

    @Test
    public void testSearchActivities() {
        Activity a1 = new Activity();
        a1.setId(1);
        when(activityMapper.searchActivities("关键字", 10)).thenReturn(Arrays.asList(a1));

        List<Activity> result = activityService.searchActivities("关键字", 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityMapper).searchActivities("关键字", 10);
    }

    @Test
    public void testSearchActivitiesNullParams() {
        when(activityMapper.searchActivities(null, null)).thenReturn(Collections.emptyList());

        List<Activity> result = activityService.searchActivities(null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(activityMapper).searchActivities(null, null);
    }

    @Test
    public void testSaveInsert() {
        Activity activity = new Activity();
        activity.setTitle("新活动");
        activity.setActivityTime(new Date());

        activityService.save(activity);

        verify(activityMapper).insert(activity);
        verify(activityMapper, never()).update(any(Activity.class));
    }

    @Test
    public void testSaveUpdate() {
        Activity activity = new Activity();
        activity.setId(1);
        activity.setTitle("旧活动");

        activityService.save(activity);

        verify(activityMapper).update(activity);
        verify(activityMapper, never()).insert(any(Activity.class));
    }

    @Test
    public void testUpdate() {
        Activity activity = new Activity();
        activity.setId(1);
        activity.setTitle("更新活动");

        activityService.update(activity);

        verify(activityMapper).update(activity);
    }

    @Test
    public void testDelete() {
        activityService.delete(1);

        verify(activityMapper).delete(1);
    }
}
