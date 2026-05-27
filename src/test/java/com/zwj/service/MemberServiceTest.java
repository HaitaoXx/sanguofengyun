package com.zwj.service;

import com.zwj.dao.MemberMapper;
import com.zwj.entity.Member;
import com.zwj.service.impl.MemberServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Member m1 = new Member();
        m1.setId(1);
        when(memberMapper.findAll()).thenReturn(Arrays.asList(m1));

        List<Member> result = memberService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberMapper).findAll();
    }

    @Test
    public void testFindAllEmpty() {
        when(memberMapper.findAll()).thenReturn(Collections.emptyList());

        List<Member> result = memberService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(memberMapper).findAll();
    }

    @Test
    public void testFindById() {
        Member member = new Member();
        member.setId(1);
        member.setUserId(10);
        when(memberMapper.findById(1)).thenReturn(member);

        Member result = memberService.findById(1);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getId());
        verify(memberMapper).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(memberMapper.findById(999)).thenReturn(null);

        Member result = memberService.findById(999);

        assertNull(result);
        verify(memberMapper).findById(999);
    }

    @Test
    public void testSaveInsert() {
        Member member = new Member();
        member.setUserId(1);
        member.setClubId(1);

        memberService.save(member);

        verify(memberMapper).insert(member);
        verify(memberMapper, never()).update(any(Member.class));
    }

    @Test
    public void testSaveUpdate() {
        Member member = new Member();
        member.setId(1);
        member.setUserId(2);

        memberService.save(member);

        verify(memberMapper).update(member);
        verify(memberMapper, never()).insert(any(Member.class));
    }

    @Test
    public void testUpdate() {
        Member member = new Member();
        member.setId(1);
        member.setRole("leader");

        memberService.update(member);

        verify(memberMapper).update(member);
    }

    @Test
    public void testDelete() {
        memberService.delete(1);

        verify(memberMapper).delete(1);
    }

    @Test
    public void testFindByClubId() {
        Member m1 = new Member();
        m1.setId(1);
        when(memberMapper.findByClubId(10)).thenReturn(Arrays.asList(m1));

        List<Member> result = memberService.findByClubId(10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberMapper).findByClubId(10);
    }

    @Test
    public void testFindByClubIdEmpty() {
        when(memberMapper.findByClubId(999)).thenReturn(Collections.emptyList());

        List<Member> result = memberService.findByClubId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(memberMapper).findByClubId(999);
    }

    @Test
    public void testFindByUserId() {
        Member m1 = new Member();
        m1.setId(1);
        when(memberMapper.findByUserId(5)).thenReturn(Arrays.asList(m1));

        List<Member> result = memberService.findByUserId(5);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberMapper).findByUserId(5);
    }

    @Test
    public void testFindByUserIdEmpty() {
        when(memberMapper.findByUserId(999)).thenReturn(Collections.emptyList());

        List<Member> result = memberService.findByUserId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(memberMapper).findByUserId(999);
    }
}
