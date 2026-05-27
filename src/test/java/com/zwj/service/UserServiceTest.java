package com.zwj.service;

import com.zwj.dao.UserMapper;
import com.zwj.entity.User;
import com.zwj.service.impl.UserServiceImpl;
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

public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        when(userMapper.findById(1)).thenReturn(user);

        User result = userService.findById(1);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getId());
        assertEquals("admin", result.getUsername());
        verify(userMapper).findById(1);
    }

    @Test
    public void testFindByIdNotFound() {
        when(userMapper.findById(999)).thenReturn(null);

        User result = userService.findById(999);

        assertNull(result);
        verify(userMapper).findById(999);
    }

    @Test
    public void testFindAll() {
        User u1 = new User();
        u1.setId(1);
        User u2 = new User();
        u2.setId(2);
        when(userMapper.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userMapper).findAll();
    }

    @Test
    public void testFindAllEmpty() {
        when(userMapper.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userMapper).findAll();
    }

    @Test
    public void testSaveInsert() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("123456");

        userService.save(user);

        verify(userMapper).insert(user);
        verify(userMapper, never()).update(any(User.class));
    }

    @Test
    public void testSaveUpdate() {
        User user = new User();
        user.setId(1);
        user.setUsername("existing");

        userService.save(user);

        verify(userMapper).update(user);
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1);
        user.setUsername("updated");

        userService.update(user);

        verify(userMapper).update(user);
    }

    @Test
    public void testDelete() {
        User user = new User();
        user.setId(1);
        user.setUsername("todelete");
        when(userMapper.findById(1)).thenReturn(user);

        userService.delete(1);

        verify(userMapper).findById(1);
        verify(userMapper).delete(1);
    }

    @Test
    public void testDeleteNotFound() {
        when(userMapper.findById(999)).thenReturn(null);

        userService.delete(999);

        verify(userMapper).findById(999);
        verify(userMapper, never()).delete(anyInt());
    }

    @Test
    public void testFindByClubId() {
        User u1 = new User();
        u1.setId(1);
        when(userMapper.findByClubId(10)).thenReturn(Arrays.asList(u1));

        List<User> result = userService.findByClubId(10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userMapper).findByClubId(10);
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        when(userMapper.findByUsername("admin")).thenReturn(user);

        User result = userService.findByUsername("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        verify(userMapper).findByUsername("admin");
    }

    @Test
    public void testFindByUsernameNotFound() {
        when(userMapper.findByUsername("nobody")).thenReturn(null);

        User result = userService.findByUsername("nobody");

        assertNull(result);
        verify(userMapper).findByUsername("nobody");
    }
}
