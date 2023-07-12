package com.cluster.service.Impl;

import com.cluster.mapper.MailLogMapper;
import com.cluster.pojo.MailLog;
import com.cluster.service.MailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MailLogServiceImpl implements MailLogService {

    @Autowired
    private MailLogMapper mailLogMapper;
    @Override
    public void updateStatus(Integer status, String msgId) {
        mailLogMapper.updateStatus(status, msgId);

    }

    @Override
    public List<MailLog> getMailLogByStatusAndTryTime(Integer status, LocalDateTime tryTime) {
        return mailLogMapper.getMailLogByStatusAndTryTime(status, tryTime);
    }

    @Override
    public void updateMailLog(Integer count, LocalDateTime updateTime, LocalDateTime tryTime, String msgId) {
         mailLogMapper.updateMailLog(count,updateTime, tryTime, msgId );
    }
}
