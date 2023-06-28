package com.cluster.mapper;

import com.cluster.pojo.Role;

import java.util.List;

public interface RoleMapper {

    List<Role> getAllRoles();

    Role getRoleById(Integer id);

    void addRole(Role role);

    Role getRoleByName(String name);



}
