package com.zwj.service;

import com.zwj.entity.Member;

import java.util.List;

public interface MemberService {
    List<Member> findAll();
    Member findById(Integer id);
    void save(Member member);
    void update(Member member);
    void delete(Integer id);
    List<Member> findByClubId(Integer clubId);
    List<Member> findByUserId(Integer userId);
}