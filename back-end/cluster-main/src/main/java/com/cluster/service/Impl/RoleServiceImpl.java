package com.cluster.service.Impl;

import com.cluster.mapper.RoleMapper;
import com.cluster.pojo.Role;
import com.cluster.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getAllRoles() {
        return null;
    }

    @Override
    public Role getRoleById(Integer id) {
        return null;
    }

    @Override
    public void addRole(Role role) {


    }

    @Override
    public Role getRoleByName(String name) {
        return roleMapper.getRoleByName(name);
    }
}
