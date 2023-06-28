package com.cluster.config.jwt;


import com.sun.jmx.remote.internal.ClientListenerInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    //jwt payload中的key，用于获取或设置对应的值
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIN_KEY_CREATED = "created";

    // 从application.properties中注入secret和expiration的值
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    //使用UserDetails对象生成新的JWT

    public String generateToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername()); //将用户名放入要生成的token的payload
        claims.put(CLAIN_KEY_CREATED, new Date());//将创建token的时间放入payload
        return generateToken(claims);

    }

    //使用创建的claim来生成新的jwt
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims) //设置payload数据
                .setExpiration(generateExpirationDate()) //设置token的过期时间
                .signWith(SignatureAlgorithm.HS512, secret) //使用HS512算法和密钥签名
                .compact(); //生成JWT字符串
    }

    //从jwt中获取用户名
    public String getUserNameFromToken(String token)
    {
        String username;
        try
        {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();//"sub"通常用于存储用户标识
        }catch(Exception e)
        {
            username = null;
        }
        return username;
    }


    //从jwt中获取payload数据
    private Claims getClaimsFromToken(String token) {

        Claims claims = null;
        try
        {
            claims = Jwts.parser()
                    .setSigningKey(secret) //需要提供签名密钥来验证jwt的合法性
                    .parseClaimsJws(token)
                    .getBody(); //获取到payload
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return claims;

    }

    //验证jwt是否有效
    public boolean validateToken(String token, UserDetails userDetails)
    {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token); //token和用户名匹配并且没过期
    }

    private boolean isTokenExpired(String token) {
        Date expireDate = getExpiredDateFromToken(token);
        return expireDate.before(new Date());
    }

    //从jwt中获取过期时间
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }


    //生成jwt的过期时间
    private Date generateExpirationDate() {

        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    //判断jwt是否可以被刷新，也就是它是否还没有过期
    public boolean canRefresh(String token)
    {
        return !isTokenExpired(token);
    }

    //刷新jwt，生成一个新的jwt并且设置创建时间为现在
    public String refreshToken(String token)
    {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIN_KEY_CREATED, new Date());
        return generateToken(claims);

    }


}
