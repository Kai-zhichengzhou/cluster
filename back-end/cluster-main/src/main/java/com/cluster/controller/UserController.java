package com.cluster.controller;


import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.User;
import com.cluster.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "返回所有用户")
    @GetMapping("/")
    public List<User> getAllUsers()
    {
        return userService.getAllUsers();
    }

    @ApiOperation(value = "搜索用户")
    @GetMapping("/{username}")
    public ApiResponse searchUser(@PathVariable String username)
    {
        try
        {
            User userInfo = userService.searchUserInfo(username);
            return (userInfo == null) ? ApiResponse.error("当前没有该用户") :ApiResponse.success("搜索成功", userInfo);
        }catch(IllegalArgumentException e)
        {
            e.printStackTrace();
            //TODO: 这里可以直接在前端做非空处理，后期可以删除
            return ApiResponse.error("当前输入的用户名为空");
        }
    }

    @ApiOperation(value = "更改用户自己的个人文档")
    @PutMapping("/updateProfile")
    public ApiResponse updateUserProfile(@RequestBody User user)
    {
        try
        {
            userService.updateUser(user.getId(), user);
            return ApiResponse.success("更改信息成功!");
        }catch(IllegalArgumentException e)
        {
            e.printStackTrace();
            return ApiResponse.error("当前更新的信息不能为空");
        }catch(AccessDeniedException e)
        {
            e.printStackTrace();
            return ApiResponse.error("你无权修改他人的信息!");
        }

    }

    @GetMapping("/myInfo")
    public ApiResponse viewMyProfile()
    {
        return ApiResponse.success("已显示个人信息",userService.getCurrentUser());
    }




}
