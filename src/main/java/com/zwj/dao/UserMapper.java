package com.zwj.dao;

import com.zwj.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    User login(@Param("username") String username, @Param("password") String password, @Param("role") String role);
    User loginByUsernamePassword(@Param("username") String username, @Param("password") String password);
    User findById(Integer id);
    List<User> findAll();
    void insert(User user);
    void update(User user);
    void delete(Integer id);
    List<User> findByClubId(Integer clubId);
    User findByUsername(String username);
    int countByRole(String role);
    int count();
}