package com.zwj.service;

import com.zwj.entity.User;

import java.util.List;

public interface UserService {
    User login(String username, String password, String role);
    User findById(Integer id);
    List<User> findAll();
    void save(User user);
    void update(User user);
    void delete(Integer id);
    List<User> findByClubId(Integer clubId);
}