package com.zwj.service;

import com.zwj.dao.ClubMapper;
import com.zwj.entity.Club;
import com.zwj.service.impl.ClubServiceImpl;
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

public class ClubServiceTest {

    @Mock
    private ClubMapper clubMapper;

    @InjectMocks
    private ClubServiceImpl clubService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Club c1 = new Club();
        c1.setId(1);
        c1.setName("篮球社");
        when(clubMapper.findAll()).thenReturn(Arrays.asList(c1));

        List<Club> result = clubService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("篮球社", result.get(0).getName());
        verify(clubMapper).findAll();
    }

    @Test
    public void testFindAllEmpty() {
        when(clubMapper.findAll()).thenReturn(Collections.emptyList());

        List<Club> result = clubService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clubMapper).findAll();
    }

    @Test
    public void testFindById() {
        Club club = new Club();
        club.setId(1);
        club.setName("足球社");
        when(clubMapper.findById(1)).thenReturn(club);

        Club result = clubService.findById(1);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getId());
        verify(clubMapper).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(clubMapper.findById(999)).thenReturn(null);

        Club result = clubService.findById(999);

        assertNull(result);
        verify(clubMapper).findById(999);
    }

    @Test
    public void testSaveInsert() {
        Club club = new Club();
        club.setName("新社团");

        clubService.save(club);

        verify(clubMapper).insert(club);
        verify(clubMapper, never()).update(any(Club.class));
    }

    @Test
    public void testSaveUpdate() {
        Club club = new Club();
        club.setId(1);
        club.setName("旧社团");

        clubService.save(club);

        verify(clubMapper).update(club);
        verify(clubMapper, never()).insert(any(Club.class));
    }

    @Test
    public void testUpdate() {
        Club club = new Club();
        club.setId(1);
        club.setName("更新社团");

        clubService.update(club);

        verify(clubMapper).update(club);
    }

    @Test
    public void testDelete() {
        clubService.delete(1);

        verify(clubMapper).delete(1);
    }

    @Test
    public void testFindByUserId() {
        Club c1 = new Club();
        c1.setId(1);
        when(clubMapper.findByUserId(5)).thenReturn(Arrays.asList(c1));

        List<Club> result = clubService.findByUserId(5);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(clubMapper).findByUserId(5);
    }

    @Test
    public void testFindByUserIdEmpty() {
        when(clubMapper.findByUserId(999)).thenReturn(Collections.emptyList());

        List<Club> result = clubService.findByUserId(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clubMapper).findByUserId(999);
    }
}
