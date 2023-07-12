package com.cluster.Task;

import com.cluster.pojo.MailLog;
import com.cluster.pojo.User;
import com.cluster.service.MailLogService;
import com.cluster.service.UserService;
import com.cluster.utils.MailConstants;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MailTask {

    @Autowired
    private MailLogService mailLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 邮件发送定时任务
     * 10秒执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask()
    {
        List<MailLog> list = mailLogService.getMailLogByStatusAndTryTime(0, LocalDateTime.now());
        list.forEach( mailLog ->
        {
            //如果重复次数超过3次，更新状态为投递失败，不再重试（0 -> 2）
            if(mailLog.getCount() >= 3)
            {
                mailLogService.updateStatus(2, mailLog.getMsgId());
            }
            //定时任务：从服务端选取还没有被确认送达的消息，然后进行定时的重发
            mailLogService.updateMailLog(mailLog.getCount()+ 1,LocalDateTime.now(), LocalDateTime.now(), mailLog.getMsgId());
            //重发需要发送的数据负载
            User currUser = userService.getUserById(mailLog.getUid());
            //重新发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, currUser, new CorrelationData(mailLog.getMsgId()));

        });

    }




}
