package com.zwj.dao;

import com.zwj.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    User login(@Param("username") String username, @Param("password") String password, @Param("role") String role);
    User findById(Integer id);
    List<User> findAll();
    void insert(User user);
    void update(User user);
    void delete(Integer id);
    List<User> findByClubId(Integer clubId);
}