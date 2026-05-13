package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Delete("DELETE FROM t_user_role WHERE user_id = #{userId}")
    void deleteByUserId(@Param("userId") Long userId);
}