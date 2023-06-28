package com.cluster.service;

import com.cluster.pojo.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    Role getRoleById(Integer id);

    void addRole(Role role);

    Role getRoleByName(String name);

}
