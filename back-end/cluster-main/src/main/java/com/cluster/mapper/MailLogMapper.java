package com.cluster.mapper;

import com.cluster.pojo.MailLog;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface MailLogMapper {

    void insert(MailLog mailLog);

    void updateStatus(@Param("status") Integer status, @Param("msgId") String msgId);

    List<MailLog> getMailLogByStatusAndTryTime(@Param("status") Integer status, @Param("tryTime")LocalDateTime tryTime);


    void updateMailLog(@Param("count") Integer count, @Param("updateTime") LocalDateTime updateTime, @Param("tryTime") LocalDateTime tryTime, @Param("msgId") String msgId);

}
