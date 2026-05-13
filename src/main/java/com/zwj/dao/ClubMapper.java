package com.zwj.dao;

import com.zwj.entity.Club;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClubMapper {
    List<Club> findAll();
    Club findById(Integer id);
    void insert(Club club);
    void update(Club club);
    void delete(Integer id);
    List<Club> findByUserId(@Param("userId") Integer userId);
}