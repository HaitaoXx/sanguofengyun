package com.gxa.service;

import com.gxa.domain.entity.Permission;
import com.gxa.domain.entity.Role;
import com.gxa.mapper.PermissionMapper;
import com.gxa.mapper.RoleMapper;
import com.gxa.mapper.RolePermissionMapper;
import com.gxa.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    public List<Role> getUserRoles(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }

    public List<Permission> getUserPermissions(Long userId) {
        return permissionMapper.selectPermissionsByUserId(userId);
    }

    public Set<String> getUserPermissionCodes(Long userId) {
        List<Permission> permissions = getUserPermissions(userId);
        return permissions.stream()
                .map(Permission::getPermissionCode)
                .collect(Collectors.toSet());
    }

    public boolean hasPermission(Long userId, String permissionCode) {
        Set<String> permissions = getUserPermissionCodes(userId);
        return permissions.contains(permissionCode);
    }

    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.deleteByUserId(userId);
        for (Long roleId : roleIds) {
            com.gxa.domain.entity.UserRole userRole = new com.gxa.domain.entity.UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
    }

    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        rolePermissionMapper.deleteByRoleId(roleId);
        for (Long permissionId : permissionIds) {
            com.gxa.domain.entity.RolePermission rolePermission = new com.gxa.domain.entity.RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);
        }
    }
}