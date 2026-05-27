package com.zwj.service.impl;

import com.zwj.dao.UserMapper;
import com.zwj.entity.User;
import com.zwj.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    @Transactional
    public void save(User user) {
        if (user.getId() == null) {
            userMapper.insert(user);
            logger.info("用户创建成功: {}", user.getUsername());
        } else {
            userMapper.update(user);
            logger.info("用户更新成功: {}", user.getUsername());
        }
    }

    @Override
    @Transactional
    public void update(User user) {
        userMapper.update(user);
        logger.info("用户信息更新: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        User user = userMapper.findById(id);
        if (user != null) {
            userMapper.delete(id);
            logger.info("用户删除成功: {}", user.getUsername());
        }
    }

    @Override
    public List<User> findByClubId(Integer clubId) {
        return userMapper.findByClubId(clubId);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}