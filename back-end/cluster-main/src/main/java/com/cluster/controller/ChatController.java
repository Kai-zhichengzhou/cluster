package com.cluster.controller;


import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.ChatMessage;
import com.cluster.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {
    @Autowired
    private ClusterService clusterService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{clusterId}/sendMessage")
    public ApiResponse sendMessage(@Payload ChatMessage chatMessage,
                                   @DestinationVariable String clusterId)
    {
        System.out.println("发消息" + chatMessage);
        clusterService.authenticateMember(Integer.parseInt(clusterId));
        System.out.println("发消息" + chatMessage);
        messagingTemplate.convertAndSend("/topic/" + clusterId, chatMessage);

        return ApiResponse.success("发送消息成功");

    }

    @MessageMapping("/chat/{clusterId}/addUser")
    public ApiResponse addUser(@Payload ChatMessage chatMessage,
                               @DestinationVariable String clusterId)
    {
        System.out.println("加用户" + chatMessage);
        System.out.println(clusterId);
        try
        {
            clusterService.authenticateMember(Integer.parseInt(clusterId));
            messagingTemplate.convertAndSend("/topic/" + clusterId, chatMessage);
            return ApiResponse.success("加入聊天成功");
        }catch(Exception e)
        {
            e.printStackTrace();
            return ApiResponse.error("你还不是该Cluster的成员,无法进入!");
        }


    }

}
