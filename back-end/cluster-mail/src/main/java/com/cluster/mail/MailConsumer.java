package com.cluster.mail;

import com.cluster.pojo.User;
import com.cluster.utils.MailConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * 消息队列消费端
 * 职责：
 * 在消息队列消费消息后将数据（邮件）发送，并且向生产者确认
 */
@Component
public class MailConsumer {

    //初始化一个logger
    private static  final Logger LOGGER = LoggerFactory.getLogger(MailConsumer.class);


    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME) //监听queue
    public void handler(Message<User> message, Channel channel)
    {
        //获取新注册的用户信息
        User user = message.getPayload();
        MessageHeaders headers = message.getHeaders();
        System.out.println(headers.toString());
        //消息序号：
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");

        HashOperations hashOperations = redisTemplate.opsForHash();

        try
        {
            //确保不重复消费
            if(hashOperations.entries("mail_log").containsKey(msgId))
            {
                LOGGER.error ("消息已被消费------------{}", msgId);
                /**
                 * 手动确认消息
                 * tag：消息序号
                 * multiple：是否确认多条
                 */
                channel.basicAck(tag, false);
                return;
            }
            //如果这条消息还没有消费，则现在处理消费逻辑，给注册的用户发送邮件
            MimeMessage msg = javaMailSender.createMimeMessage();
            //通过MimeMessageHelper来设置email
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            //发件人
            helper.setFrom(mailProperties.getUsername());
            //收件人
            helper.setTo(user.getEmail());
            helper.setSubject("注册成功邮件：欢迎来到Cluster宇宙! ");
            helper.setSentDate(new Date());
            //邮件内容
            Context context = new Context();
            context.setVariable("username", user.getUsername());
            context.setVariable("name",user.getName());
            context.setVariable("signature", user.getSignature());
            context.setVariable("role", user.getRole().getNameZh());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            //发送邮件
            javaMailSender.send(msg);
            LOGGER.info("邮件发送成功");
            //将消息id存入redis
            hashOperations.put("mail_log", msgId, "OK");
            System.out.println("redis代码运行");

            //手动确认消息
            channel.basicAck(tag, false);

        }catch(Exception e)
        {
            /**
             * 手动确认消息
             */
            try{
                channel.basicNack(tag, false, true);
            } catch (IOException ioException) {
                LOGGER.error("邮件发送异常---------------->{}",e.getMessage());
            }
            LOGGER.error("邮件发送异常---------------->{}",e.getMessage());

        }


    }




}
