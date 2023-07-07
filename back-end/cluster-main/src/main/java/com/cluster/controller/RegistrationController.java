package com.cluster.controller;


import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.User;
import com.cluster.service.RoleService;
import com.cluster.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/registration")
public class RegistrationController {


    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/signup")
    public ApiResponse registerUser(@RequestBody User newUser)
    {
        User existUser = userService.getUserByUsername(newUser.getUsername());
        if(existUser != null)
        {
            return ApiResponse.error("很抱歉, 用户名已被注册:(");
        }

        //当前用户可以注册
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(roleService.getRoleByName("ROLE_user"));
        newUser.setEnabled(true);
        newUser.setDateJoined(new Date());


        //存储当前用户信息
        System.out.println(newUser);
        System.out.println(userService);
        boolean res = userService.registerUser(newUser);
        return (res) ? ApiResponse.success("用户注册成功！") : ApiResponse.error("很抱歉，注册失败");

    }


}
