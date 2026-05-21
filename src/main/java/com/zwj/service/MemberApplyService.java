package com.zwj.service;

import com.zwj.entity.MemberApply;

import java.util.List;

public interface MemberApplyService {
    void apply(Integer clubId, Integer userId);
    void approve(Integer id);
    void reject(Integer id);
    void delete(Integer id);
    MemberApply findById(Integer id);
    List<MemberApply> findByClubId(Integer clubId);
    List<MemberApply> findByUserId(Integer userId);
    boolean hasApplied(Integer clubId, Integer userId);
}
