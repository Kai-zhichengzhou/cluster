package com.cluster.service.Impl;

import com.cluster.config.jwt.JwtUtil;
import com.cluster.exception.RecordNotFoundException;
import com.cluster.mapper.MailLogMapper;
import com.cluster.mapper.RoleMapper;
import com.cluster.mapper.UserMapper;
import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.MailLog;
import com.cluster.pojo.Role;
import com.cluster.pojo.User;
import com.cluster.service.PaginationService;
import com.cluster.service.UserService;
import com.cluster.utils.MailConstants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    PaginationService paginationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MailLogMapper mailLogMapper;

    @Value("${server.url}")
    private String serverUrl;
    @Value("${file.storage-location}")
    private String location;



    @Override
    public ApiResponse login(String username, String password, String code, HttpServletRequest request) {


        System.out.println(username);
        System.out.println(password);
        System.out.println(code);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        if(userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword()))
        {
            return ApiResponse.error("用户名或密码输入错误，请重新尝试!");
        }
//        String captcha = (String) request.getSession().getAttribute("captcha");
//        System.out.println("c:" + captcha);
//        if(StringUtils.isEmpty(code) || !captcha.equals(code))
//        {
//            return ApiResponse.error("验证码填写错误");
//        }

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
        User user = (User)redisTemplate.opsForValue().get("user:"+id);
        if(user == null)
        {
            //说明redis缓存中没有
            System.out.println("redis缓存没有命中");
            user = userMapper.getUserById(id);
            redisTemplate.opsForValue().set("user:"+id,user);
        }
        return user;
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
        User user = userMapper.searchUserInfoByUsername(username);
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
    public boolean registerUser(User user) {

        userMapper.registerUser(user);
        Integer id = user.getId();
        if(id != null) //说明插入成功
        {
            userMapper.insertUserRole(id, user.getRole().getId());
            User currUser = userMapper.getUserById(id);
            //通过数据库来记录现在发送的消息
            //生成要发送的消息
            //生成一个唯一的messageId
            String msgId = UUID.randomUUID().toString();
            MailLog mailLog = new MailLog();
            mailLog.setMsgId(msgId);
            mailLog.setUid(id);
            mailLog.setStatus(0);
            mailLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailLog.setCount(0);
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT));
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());
            //生成成功消息之后将这条mail log添加到持久层
            mailLogMapper.insert(mailLog);

            //通过rabbitmq框架发送消息
            //当前server作为消息的producer
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_NAME,
                    currUser, new CorrelationData(msgId));

            return true;
        }
        return false;
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
        System.out.println("数据修改，所以删除redis中的键值对");
        redisTemplate.delete("user:" + id);
    }

    @Override
    public User getCurrentUser()
    {
        User currUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) redisTemplate.opsForValue().get("user:"+currUser.getId());
        if(user == null)
        {
            user = userMapper.getUserById(currUser.getId());
            redisTemplate.opsForValue().set("user:" + currUser.getId(), user);
        }
        return  user;
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
        redisTemplate.delete("user:" + id);

    }

    @Override
    public Role getRoleByUserId(Integer id) {

        User user = (User) redisTemplate.opsForValue().get("user:" + id);
        if(user == null)
        {
            return userMapper.getRoleByUserId(id);
        }
        return user.getRole();

    }

    @Override
    public PageInfo<User> getUserByPage(Integer page, Integer size) {

       PageInfo<User> pageInfo =  paginationService.getEntityByPage(page, size, () -> userMapper.getAllUsers());

        return pageInfo;
    }

    @Override
    public void uploadAvatar(Integer id, String avatar) {
        redisTemplate.delete("user:" + id);
        userMapper.uploadAvatar(id, avatar);
    }

    @Override
    public String getFullAvatarUrl(String avatarPath) {
        StringBuilder sb = new StringBuilder();
        sb.append("/avatar/uploads/");
        sb.append(avatarPath);
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( serverUrl)
//                .path("/avatar/uploads/")
//                .pathSegment(avatarPath);


        return sb.toString();
    }
}
