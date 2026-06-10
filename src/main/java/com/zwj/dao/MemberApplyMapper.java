package com.zwj.dao;

import com.zwj.entity.MemberApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberApplyMapper {
    void insert(MemberApply apply);
    void update(MemberApply apply);
    void delete(Integer id);
    MemberApply findById(Integer id);
    List<MemberApply> findByClubId(Integer clubId);
    List<MemberApply> findByUserId(Integer userId);
    MemberApply findByClubIdAndUserId(@Param("clubId") Integer clubId, @Param("userId") Integer userId);
    List<MemberApply> findAll();
    int count();
}
