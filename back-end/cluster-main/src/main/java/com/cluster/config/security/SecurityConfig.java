package com.cluster.config.security;


import com.cluster.config.jwt.JwtAuthenticationTokenFilter;
import com.cluster.config.jwt.OtherAuthenticationEntryPoint;
import com.cluster.config.jwt.RestfulAccessDeniedHandler;
import com.cluster.pojo.User;
import com.cluster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserService userService;

    @Autowired
    private OtherAuthenticationEntryPoint otherAuthenticationEntryPoint;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //使用jwt不需要csrf
        http.csrf()
                .disable()
                //基于token，不需要session
                .sessionManagement()
                //基于token，不需要session
                // 这里设置的是会话管理策略（SessionCreationPolicy）为无状态，也就是说Spring Security不会创建也不会使用session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //允许对/login, /logout不需要任何访问限制
                .antMatchers("/registration/signup").permitAll()
//                .antMatchers("/admin/**").hasRole("admin")
//                .anyRequest().hasRole("user")
                .anyRequest().authenticated()
                .and().headers().cacheControl();

        //添加jwt过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(otherAuthenticationEntryPoint);

    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        //放行静态资源
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "/favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha");

    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService()
    {
        return username ->
        {
            User user =userService.getUserByUsername(username);
            if(user != null)
            {
                user.setRole(userService.getRoleByUserId(user.getId()));
                return user;
            }
            return null;

        };

    }

    //注册JWT过滤器
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter()
    {
        return new JwtAuthenticationTokenFilter();
    }
}
