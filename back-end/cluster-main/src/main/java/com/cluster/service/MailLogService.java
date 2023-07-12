package com.cluster.service;

import com.cluster.pojo.MailLog;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MailLogService {

    void updateStatus(Integer status, String msgId);

    List<MailLog> getMailLogByStatusAndTryTime(Integer status, LocalDateTime tryTime);

    void updateMailLog( Integer count,  LocalDateTime updateTime, LocalDateTime tryTime, String msgId);
}
