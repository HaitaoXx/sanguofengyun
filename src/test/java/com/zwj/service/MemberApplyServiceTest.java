package com.zwj.service;

import com.zwj.dao.MemberApplyMapper;
import com.zwj.entity.MemberApply;
import com.zwj.service.impl.MemberApplyServiceImpl;
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

public class MemberApplyServiceTest {

    @Mock
    private MemberApplyMapper memberApplyMapper;

    @InjectMocks
    private MemberApplyServiceImpl memberApplyService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApply() {
        memberApplyService.apply(1, 2);

        ArgumentCaptor<MemberApply> captor = ArgumentCaptor.forClass(MemberApply.class);
        verify(memberApplyMapper).insert(captor.capture());
        MemberApply captured = captor.getValue();
        assertEquals(Integer.valueOf(1), captured.getClubId());
        assertEquals(Integer.valueOf(2), captured.getUserId());
        assertEquals("pending", captured.getStatus());
    }

    @Test
    public void testApprove() {
        MemberApply apply = new MemberApply();
        apply.setId(1);
        apply.setStatus("pending");
        when(memberApplyMapper.findById(1)).thenReturn(apply);

        memberApplyService.approve(1);

        assertEquals("approved", apply.getStatus());
        verify(memberApplyMapper).update(apply);
    }

    @Test
    public void testApproveNotFound() {
        when(memberApplyMapper.findById(999)).thenReturn(null);

        memberApplyService.approve(999);

        verify(memberApplyMapper, never()).update(any(MemberApply.class));
    }

    @Test
    public void testReject() {
        MemberApply apply = new MemberApply();
        apply.setId(1);
        apply.setStatus("pending");
        when(memberApplyMapper.findById(1)).thenReturn(apply);

        memberApplyService.reject(1);

        assertEquals("rejected", apply.getStatus());
        verify(memberApplyMapper).update(apply);
    }

    @Test
    public void testRejectNotFound() {
        when(memberApplyMapper.findById(999)).thenReturn(null);

        memberApplyService.reject(999);

        verify(memberApplyMapper, never()).update(any(MemberApply.class));
    }

    @Test
    public void testDelete() {
        memberApplyService.delete(1);

        verify(memberApplyMapper).delete(1);
    }

    @Test
    public void testFindById() {
        MemberApply apply = new MemberApply();
        apply.setId(1);
        when(memberApplyMapper.findById(1)).thenReturn(apply);

        MemberApply result = memberApplyService.findById(1);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getId());
        verify(memberApplyMapper).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(memberApplyMapper.findById(999)).thenReturn(null);

        MemberApply result = memberApplyService.findById(999);

        assertNull(result);
        verify(memberApplyMapper).findById(999);
    }

    @Test
    public void testFindByClubId() {
        MemberApply a1 = new MemberApply();
        a1.setId(1);
        when(memberApplyMapper.findByClubId(10)).thenReturn(Arrays.asList(a1));

        List<MemberApply> result = memberApplyService.findByClubId(10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberApplyMapper).findByClubId(10);
    }

    @Test
    public void testFindByClubIdEmpty() {
        when(memberApplyMapper.findByClubId(999)).thenReturn(Collections.emptyList());

        List<MemberApply> result = memberApplyService.findByClubId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(memberApplyMapper).findByClubId(999);
    }

    @Test
    public void testFindByUserId() {
        MemberApply a1 = new MemberApply();
        a1.setId(1);
        when(memberApplyMapper.findByUserId(5)).thenReturn(Arrays.asList(a1));

        List<MemberApply> result = memberApplyService.findByUserId(5);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberApplyMapper).findByUserId(5);
    }

    @Test
    public void testFindByUserIdEmpty() {
        when(memberApplyMapper.findByUserId(999)).thenReturn(Collections.emptyList());

        List<MemberApply> result = memberApplyService.findByUserId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(memberApplyMapper).findByUserId(999);
    }

    @Test
    public void testHasAppliedTrue() {
        MemberApply apply = new MemberApply();
        apply.setId(1);
        when(memberApplyMapper.findByClubIdAndUserId(1, 2)).thenReturn(apply);

        boolean result = memberApplyService.hasApplied(1, 2);

        assertTrue(result);
        verify(memberApplyMapper).findByClubIdAndUserId(1, 2);
    }

    @Test
    public void testHasAppliedFalse() {
        when(memberApplyMapper.findByClubIdAndUserId(1, 2)).thenReturn(null);

        boolean result = memberApplyService.hasApplied(1, 2);

        assertFalse(result);
        verify(memberApplyMapper).findByClubIdAndUserId(1, 2);
    }
}
