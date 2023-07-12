package com.cluster.config.security;

import com.cluster.service.MailLogService;
import com.cluster.utils.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置消息队列
 *
 */
@Configuration
public class RabbitMQConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private MailLogService mailLogService;

    //发送消息回调
    //设置回调函数
    @Bean
    public RabbitTemplate rabbitTemplate()
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         * 消息确认回调，确认消息是否到达 broker
         *
         */
        rabbitTemplate.setConfirmCallback( (data, ack, cause) ->
        {
            String msgId = data.getId();
            if(ack)
            {
                logger.info("{}--- 消息发送成功",msgId);
                mailLogService.updateStatus(1,msgId);
            }else
            {
                logger.error("{}------消息发送失败",msgId);
            }

        });
        /**
         * 消息失败回调，比如 router 不到 queue 时回调
         * msg : 消息主题
         * responseCode : 响应码
         * reponseText: 相应描述
         * exchange: 交换机
         * routekey : 路由键
         */
        rabbitTemplate.setReturnCallback( (msg, responseCode, responseText, exchange, routeKey) ->
        {
            logger.error("{} ----- 消息发送失败", msg.getBody());

        });
        return rabbitTemplate;
    }

    @Bean
    public Queue queue()
    {
        return new Queue(MailConstants.MAIL_QUEUE_NAME);

    }

    @Bean
    public DirectExchange directExchange()
    {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }
    @Bean
    public Binding binding()
    {
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);

    }




}
