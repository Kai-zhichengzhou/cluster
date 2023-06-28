package com.cluster.config.jwt;

import com.cluster.pojo.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class OtherAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        //设置响应的字符编码encoding
        response.setCharacterEncoding("UTF-8");

        //设置响应的内容类型为json
        response.setContentType("application/json");

        //获取response的写入对象
        PrintWriter out  = response.getWriter();

        ApiResponse apiResponse = ApiResponse.error("很抱歉，您的权限不足，请联系客服");
        apiResponse.setCode(401);

        //通过写入对象写入到response
        out.write(new ObjectMapper().writeValueAsString(apiResponse));
        out.flush();
        out.close();

    }

}
