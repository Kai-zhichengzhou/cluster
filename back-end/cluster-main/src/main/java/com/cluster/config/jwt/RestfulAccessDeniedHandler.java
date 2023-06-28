package com.cluster.config.jwt;

import com.cluster.pojo.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        ApiResponse apiResponse = ApiResponse.error("很抱歉，您的会员权限不足，请联系客服");
        apiResponse.setCode(403);
        out.write(new ObjectMapper().writeValueAsString(apiResponse));

        out.flush();
        out.close();

    }
}
