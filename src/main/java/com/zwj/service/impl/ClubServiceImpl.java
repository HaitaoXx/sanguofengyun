package com.zwj.service.impl;

import com.zwj.dao.ClubMapper;
import com.zwj.entity.Club;
import com.zwj.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {
    @Autowired
    private ClubMapper clubMapper;

    @Override
    public List<Club> findAll() {
        return clubMapper.findAll();
    }

    @Override
    public Club findById(Integer id) {
        return clubMapper.findById(id);
    }

    @Override
    public void save(Club club) {
        if (club.getId() == null) {
            clubMapper.insert(club);
        } else {
            clubMapper.update(club);
        }
    }

    @Override
    public void update(Club club) {
        clubMapper.update(club);
    }

    @Override
    public void delete(Integer id) {
        clubMapper.delete(id);
    }

    @Override
    public List<Club> findByUserId(Integer userId) {
        return clubMapper.findByUserId(userId);
    }
}