package com.zwj.service;

import com.zwj.entity.User;

import java.util.List;

public interface UserService {
    /**
     * @deprecated 使用 loginByUsernamePassword 配合 PasswordEncoder 替代
     */
    @Deprecated
    User login(String username, String password, String role);

    /**
     * @deprecated 使用 findByUsername 配合 PasswordEncoder 替代
     */
    @Deprecated
    User loginByUsernamePassword(String username, String password);

    User findById(Integer id);
    List<User> findAll();
    void save(User user);
    void update(User user);
    void delete(Integer id);
    List<User> findByClubId(Integer clubId);
    User findByUsername(String username);
}
