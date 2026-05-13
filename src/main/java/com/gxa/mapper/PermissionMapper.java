package com.gxa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxa.domain.entity.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("SELECT DISTINCT p.* FROM t_permission p " +
            "JOIN t_role_permission rp ON p.id = rp.permission_id " +
            "JOIN t_user_role ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<Permission> selectPermissionsByUserId(@Param("userId") Long userId);

    @Select("SELECT DISTINCT p.* FROM t_permission p " +
            "JOIN t_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<Permission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
}