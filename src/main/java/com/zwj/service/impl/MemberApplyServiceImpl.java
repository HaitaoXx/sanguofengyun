package com.zwj.service.impl;

import com.zwj.dao.MemberApplyMapper;
import com.zwj.dao.MemberMapper;
import com.zwj.entity.Member;
import com.zwj.entity.MemberApply;
import com.zwj.service.MemberApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberApplyServiceImpl implements MemberApplyService {
    @Autowired
    private MemberApplyMapper memberApplyMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    @Transactional
    public void apply(Integer clubId, Integer userId) {
        MemberApply apply = new MemberApply();
        apply.setClubId(clubId);
        apply.setUserId(userId);
        apply.setStatus("pending");
        memberApplyMapper.insert(apply);
    }

    @Override
    @Transactional
    public void approve(Integer id) {
        MemberApply apply = memberApplyMapper.findById(id);
        if (apply != null) {
            apply.setStatus("approved");
            memberApplyMapper.update(apply);
            // 创建成员记录
            Member member = new Member();
            member.setUserId(apply.getUserId());
            member.setClubId(apply.getClubId());
            member.setRole("成员");
            memberMapper.insert(member);
        }
    }

    @Override
    @Transactional
    public void reject(Integer id) {
        MemberApply apply = memberApplyMapper.findById(id);
        if (apply != null) {
            apply.setStatus("rejected");
            memberApplyMapper.update(apply);
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        memberApplyMapper.delete(id);
    }

    @Override
    public MemberApply findById(Integer id) {
        return memberApplyMapper.findById(id);
    }

    @Override
    public List<MemberApply> findByClubId(Integer clubId) {
        return memberApplyMapper.findByClubId(clubId);
    }

    @Override
    public List<MemberApply> findByUserId(Integer userId) {
        return memberApplyMapper.findByUserId(userId);
    }

    @Override
    public boolean hasApplied(Integer clubId, Integer userId) {
        MemberApply apply = memberApplyMapper.findByClubIdAndUserId(clubId, userId);
        return apply != null;
    }
}
