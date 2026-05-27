package com.zwj.service.impl;

import com.zwj.dao.MemberApplyMapper;
import com.zwj.entity.MemberApply;
import com.zwj.service.MemberApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberApplyServiceImpl implements MemberApplyService {
    @Autowired
    private MemberApplyMapper memberApplyMapper;

    @Override
    public void apply(Integer clubId, Integer userId) {
        MemberApply apply = new MemberApply();
        apply.setClubId(clubId);
        apply.setUserId(userId);
        apply.setStatus("pending");
        memberApplyMapper.insert(apply);
    }

    @Override
    public void approve(Integer id) {
        MemberApply apply = memberApplyMapper.findById(id);
        if (apply != null) {
            apply.setStatus("approved");
            memberApplyMapper.update(apply);
        }
    }

    @Override
    public void reject(Integer id) {
        MemberApply apply = memberApplyMapper.findById(id);
        if (apply != null) {
            apply.setStatus("rejected");
            memberApplyMapper.update(apply);
        }
    }

    @Override
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
