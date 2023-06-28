package com.cluster.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//自定义的jwt 认证过滤器，保证一次请求只通过一次filter，不会重复执行
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private UserDetailsService userDetailsService;

    //jwt工具类
    @Autowired
    private JwtUtil jwtUtil;

    //从配置文件读取的JWT的header的key
    @Value(value = "${jwt.tokenHeader}")
    private String tokenHeader;
    //从配置文件读取的JWT的header的value的开头部分
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    //核心的过滤逻辑
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            //首先从发来的request获取request header的jwt
            String authHeader = request.getHeader(tokenHead);
            //如果request的header里有jwt，那么服务器对它进行解析，设置authentication的信息
            if(authHeader != null && authHeader.startsWith(tokenHead))
            {
                //去掉头部的bearer，获取真正的token部分
                String authToken = authHeader.substring(tokenHead.length());
                //根据获得的token获取用户名
                String username = jwtUtil.getUserNameFromToken(authToken);
                //如果在当前的security上下文中没有该用户的认证信息，则要第一次对它进行认证
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
                {
                    //根据用户名获取UserDetail对象
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    //验证token是否有效，对应用户是否存在
                    if(jwtUtil.validateToken(authToken, userDetails))
                    {
                        //构造UsernamePasswordAuthenticationToken对象，spring security根据这个对象进行授权和认证
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        //把request的信息设置到usernamepasswordauthenticationtoken
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        //将认证信息设置到上下文
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                }

            }
            //最后将请求传递给下一个过滤器
            filterChain.doFilter(request, response);

    }
}
