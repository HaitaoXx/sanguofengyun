package com.zwj.service;

import com.zwj.dao.ActivityApplyMapper;
import com.zwj.entity.ActivityApply;
import com.zwj.service.impl.ActivityApplyServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ActivityApplyServiceTest {

    @Mock
    private ActivityApplyMapper activityApplyMapper;

    @InjectMocks
    private ActivityApplyServiceImpl activityApplyService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApply() {
        activityApplyService.apply(1, 2);

        ArgumentCaptor<ActivityApply> captor = ArgumentCaptor.forClass(ActivityApply.class);
        verify(activityApplyMapper).insert(captor.capture());
        ActivityApply captured = captor.getValue();
        assertEquals(Integer.valueOf(1), captured.getActivityId());
        assertEquals(Integer.valueOf(2), captured.getUserId());
        assertEquals("pending", captured.getStatus());
    }

    @Test
    public void testUpdateStatus() {
        ActivityApply apply = new ActivityApply();
        apply.setId(1);
        apply.setStatus("pending");
        when(activityApplyMapper.findById(1)).thenReturn(apply);

        activityApplyService.updateStatus(1, "approved");

        assertEquals("approved", apply.getStatus());
        verify(activityApplyMapper).update(apply);
    }

    @Test
    public void testUpdateStatusNotFound() {
        when(activityApplyMapper.findById(999)).thenReturn(null);

        activityApplyService.updateStatus(999, "approved");

        verify(activityApplyMapper, never()).update(any(ActivityApply.class));
    }

    @Test
    public void testDelete() {
        activityApplyService.delete(1);

        verify(activityApplyMapper).delete(1);
    }

    @Test
    public void testFindById() {
        ActivityApply apply = new ActivityApply();
        apply.setId(1);
        when(activityApplyMapper.findById(1)).thenReturn(apply);

        ActivityApply result = activityApplyService.findById(1);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getId());
        verify(activityApplyMapper).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(activityApplyMapper.findById(999)).thenReturn(null);

        ActivityApply result = activityApplyService.findById(999);

        assertNull(result);
        verify(activityApplyMapper).findById(999);
    }

    @Test
    public void testFindByActivityId() {
        ActivityApply a1 = new ActivityApply();
        a1.setId(1);
        when(activityApplyMapper.findByActivityId(10)).thenReturn(Arrays.asList(a1));

        List<ActivityApply> result = activityApplyService.findByActivityId(10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityApplyMapper).findByActivityId(10);
    }

    @Test
    public void testFindByActivityIdEmpty() {
        when(activityApplyMapper.findByActivityId(999)).thenReturn(Collections.emptyList());

        List<ActivityApply> result = activityApplyService.findByActivityId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(activityApplyMapper).findByActivityId(999);
    }

    @Test
    public void testFindByUserId() {
        ActivityApply a1 = new ActivityApply();
        a1.setId(1);
        when(activityApplyMapper.findByUserId(5)).thenReturn(Arrays.asList(a1));

        List<ActivityApply> result = activityApplyService.findByUserId(5);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityApplyMapper).findByUserId(5);
    }

    @Test
    public void testFindByUserIdEmpty() {
        when(activityApplyMapper.findByUserId(999)).thenReturn(Collections.emptyList());

        List<ActivityApply> result = activityApplyService.findByUserId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(activityApplyMapper).findByUserId(999);
    }

    @Test
    public void testHasAppliedTrue() {
        ActivityApply apply = new ActivityApply();
        apply.setId(1);
        when(activityApplyMapper.findByActivityIdAndUserId(1, 2)).thenReturn(apply);

        boolean result = activityApplyService.hasApplied(1, 2);

        assertTrue(result);
        verify(activityApplyMapper).findByActivityIdAndUserId(1, 2);
    }

    @Test
    public void testHasAppliedFalse() {
        when(activityApplyMapper.findByActivityIdAndUserId(1, 2)).thenReturn(null);

        boolean result = activityApplyService.hasApplied(1, 2);

        assertFalse(result);
        verify(activityApplyMapper).findByActivityIdAndUserId(1, 2);
    }
}
