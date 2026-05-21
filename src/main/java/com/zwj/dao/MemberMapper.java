package com.zwj.dao;

import com.zwj.entity.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberMapper {
    List<Member> findAll();
    Member findById(Integer id);
    void insert(Member member);
    void update(Member member);
    void delete(Integer id);
    List<Member> findByClubId(@Param("clubId") Integer clubId);
    List<Member> findByUserId(@Param("userId") Integer userId);
}