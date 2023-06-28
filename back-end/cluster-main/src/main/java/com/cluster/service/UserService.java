package com.cluster.service;

import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.Role;
import com.cluster.pojo.User;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户的业务层接口
 * 定义用户相关的业务逻辑方法
 */
public interface UserService {

    User getUserById(Integer id);

    List<User> getAllUsers();

    User getUserByUsername(String username);

    void registerUser(User user);

    void updateUser(User user);

    void addRankPoint(@Param("id") Integer id, @Param("rank") Integer rank);

    void deleteUser(Integer id);

    Role getRoleByUserId(Integer id);


    ApiResponse login(String username, String password, String code, HttpServletRequest request);
}