package com.zwj.service.impl;

import com.zwj.dao.MemberMapper;
import com.zwj.entity.Member;
import com.zwj.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public List<Member> findAll() {
        return memberMapper.findAll();
    }

    @Override
    public Member findById(Integer id) {
        return memberMapper.findById(id);
    }

    @Override
    public void save(Member member) {
        if (member.getId() == null) {
            memberMapper.insert(member);
        } else {
            memberMapper.update(member);
        }
    }

    @Override
    public void update(Member member) {
        memberMapper.update(member);
    }

    @Override
    public void delete(Integer id) {
        memberMapper.delete(id);
    }

    @Override
    public List<Member> findByClubId(Integer clubId) {
        return memberMapper.findByClubId(clubId);
    }

    @Override
    public List<Member> findByUserId(Integer userId) {
        return memberMapper.findByUserId(userId);
    }
}