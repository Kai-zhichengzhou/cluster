package com.cluster.controller;


import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.LoginRequest;
import com.cluster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request)
    {        return userService.login(loginRequest.getUsername(), loginRequest.
getPassword(), loginRequest.getCode(),request);

    }



}
