package com.cluster.mapper;

import com.cluster.pojo.Role;
import com.cluster.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {


    User getUserById(Integer id);

    List<User> getAllUsers();

    User getUserByUsername(String username);

    void registerUser(User user);

    void insertUserRole(@Param("uid") Integer uid, @Param("rid") Integer rid);

    void updateUser(User user);

    void addRankPoint(@Param("id") Integer id, @Param("rank") Integer rank);

    void deleteUser(Integer id);

    Role getRoleByUserId(Integer id);


}
