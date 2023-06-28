package com.cluster.service.Impl;

import com.cluster.config.jwt.JwtUtil;
import com.cluster.mapper.RoleMapper;
import com.cluster.mapper.UserMapper;
import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.Role;
import com.cluster.pojo.User;
import com.cluster.service.UserService;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;



    @Override
    public ApiResponse login(String username, String password, String code, HttpServletRequest request) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        if(userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword()))
        {
            return ApiResponse.error("用户名或密码输入错误，请重新尝试!");
        }
        String captcha = (String) request.getSession().getAttribute("captcha");
        if(StringUtils.isEmpty(code) || !captcha.equals(code))
        {
            return ApiResponse.error("验证码填写错误");
        }

        //登陆成功后更新security登陆用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //生成jwt token
        String token = jwtUtil.generateToken(userDetails);
        Map<String,Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return ApiResponse.success("登陆成功！", tokenMap);


    }


    @Override
    public User getUserById(Integer id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {

        return userMapper.getUserByUsername(username);
    }

    /**
     * 注册用户的业务逻辑：
     * 1.存储用户信息
     * 2.更新用户_权限的中间表
     * @param user
     */
    @Override
    @Transactional
    public void registerUser(User user) {
        userMapper.registerUser(user);
        Integer id = userMapper.getUserByUsername(user.getUsername()).getId();
        userMapper.insertUserRole(id, user.getRole().getId());
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void addRankPoint(Integer id, Integer rank) {

    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public Role getRoleByUserId(Integer id) {

        return userMapper.getRoleByUserId(id);
    }
}
