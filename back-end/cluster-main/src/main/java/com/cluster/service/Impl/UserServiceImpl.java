package com.cluster.service.Impl;

import com.cluster.config.jwt.JwtUtil;
import com.cluster.exception.RecordNotFoundException;
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
import org.springframework.security.access.AccessDeniedException;
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

        return userMapper.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {

        return userMapper.getAllUsers();
    }


    /**
     * 获取当前用户的等级积分
     * @param id
     * @return
     */
    @Override
    public Integer getUserRank(Integer id) {
        return userMapper.getUserRank(id);
    }

    @Override
    public User getUserByUsername(String username) {

        return userMapper.getUserByUsername(username);
    }

    @Override
    public User searchUserInfo(String username) {
        if(username == null)
        {
            throw new IllegalArgumentException("搜索的用户名不能为空");
        }
        User user = userMapper.searchUserInfo(username);
        return user;
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


    /**
     * 修改用户信息
     * @param user
     */
    @Override
    @Transactional
    public void updateUser(Integer id,User user){
        if(user == null)
        {
            throw new IllegalArgumentException("你当前的修改信息不能为空");
        }
        User currUser = getCurrentUser();
        if(!id.equals(currUser.getId()))
        {
            throw new AccessDeniedException("你没有权限修改他人的信息!");
        }
        userMapper.updateUser(user);

    }

    @Override
    public User getCurrentUser()
    {
        User currUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.getUserById(currUser.getId());
    }

    /**
     * 增加用户的等级积分
     * @param id
     * @param pointsToAdd
     */
    @Override
    @Transactional
    public void addRankPoint(Integer id, Integer pointsToAdd) {

        Integer currRank = getUserRank(id);
        userMapper.addRankPoint(id, currRank + pointsToAdd);

    }


    /**
     * 删除当前用户
     * @param id
     */
    @Override
    @Transactional
    public void deleteUser(Integer id) {
        int rowAffected = userMapper.deleteUser(id);
        if(rowAffected == 0)
        {
            throw new RecordNotFoundException("当前要删除的用户不存在!");
        }

    }

    @Override
    public Role getRoleByUserId(Integer id) {

        return userMapper.getRoleByUserId(id);
    }
}
