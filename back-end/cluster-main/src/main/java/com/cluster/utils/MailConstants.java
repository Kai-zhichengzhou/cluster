package com.cluster.utils;


public class MailConstants {

    public static final Integer DELIVERING = 0;

    public static final Integer SUCCESS = 1;

    public static final Integer FAILURE = 2;

    public static final Integer MAX_TRY_COUNT = 4;

    public static final Integer MSG_TIMEOUT = 1; // 1分钟

    //定义消息队列
    public static final String MAIL_QUEUE_NAME = "cluster.mail.queue";
    //定义交换机
    public static final String MAIL_EXCHANGE_NAME = "cluster.mail.exchange";
    //定义路由键
    public static final String MAIL_ROUTING_KEY_NAME = "cluster.mail.routing.key";



}
