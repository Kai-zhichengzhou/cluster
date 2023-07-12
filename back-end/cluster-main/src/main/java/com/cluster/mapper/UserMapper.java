package com.cluster.mapper;

import com.cluster.pojo.Role;
import com.cluster.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {


    User getUserById(Integer id);

    List<User> getAllUsers();

    User getUserByUsername(String username);

    List<User> searchUserInfo(String name);

    Integer getUserRank(Integer id);
    void registerUser(User user);

    void insertUserRole(@Param("uid") Integer uid, @Param("rid") Integer rid);

    void updateUser(User user);

    void uploadAvatar(@Param("id") Integer id, @Param("avatar") String avatar);

    void addRankPoint(@Param("id") Integer id, @Param("rank") Integer rank);

    Integer deleteUser(Integer id);

    Role getRoleByUserId(Integer id);

    User searchUserInfoByUsername(String username);


}
