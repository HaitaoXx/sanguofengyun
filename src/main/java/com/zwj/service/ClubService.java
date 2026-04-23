package com.zwj.service;

import com.zwj.entity.Club;

import java.util.List;

public interface ClubService {
    List<Club> findAll();
    Club findById(Integer id);
    void save(Club club);
    void update(Club club);
    void delete(Integer id);
    List<Club> findByUserId(Integer userId);
}