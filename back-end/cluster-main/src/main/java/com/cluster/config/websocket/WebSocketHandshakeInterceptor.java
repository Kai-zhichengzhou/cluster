//package com.cluster.config.websocket;
//
//import com.cluster.config.jwt.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//@Component
//public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
//
//    //注入JwtUtil
//    @Autowired
//    private JwtUtil jwtUtil;
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//
//
//        System.out.println("starts handshake1");
//        if (request instanceof ServletServerHttpRequest) {
//            //将请求转为http请求
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//            HttpServletRequest httpRequest = servletRequest.getServletRequest();
//            String authHeader = httpRequest.getParameter("token");
////            System.out.println(httpRequest.getRequestURL());
//
//            System.out.println("starts handshake2");
////            String authHeader = httpRequest.getHeader("Authorization");
//            System.out.println(authHeader);
//
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                //提取jwt token
//                String jwtToken = authHeader.substring(7);
//                String username = jwtUtil.getUserNameFromToken(jwtToken);
//
//                //根据用户名获取UserDetail对象
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                //验证jwt token的有效性
//                if (jwtUtil.validateToken(jwtToken, userDetails)) {
//                    // 将用户名放到WebSocketSession的attributes，方便后续使用
//                    attributes.put("username", username);
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    // 将认证对象存入 SecurityContextHolder
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    System.out.println("验证成功");
//                    return true;
//
//                }
//            }
//        }
//        //返回true表示握手成功
//        return false;
//    }
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                               WebSocketHandler wsHandler, Exception ex) {
//        // 握手后的处理，通常不需要做什么
//
//        System.out.println("握手成功后的输出");
//
//    }
//}
