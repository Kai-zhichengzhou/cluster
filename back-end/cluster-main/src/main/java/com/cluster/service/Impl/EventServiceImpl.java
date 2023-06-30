package com.cluster.service.Impl;

import com.cluster.exception.RecordNotFoundException;
import com.cluster.mapper.EventMapper;
import com.cluster.pojo.Cluster;
import com.cluster.pojo.Event;
import com.cluster.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventMapper eventMapper;


    /**
     * 获取事件
     * @param id
     * @return
     */
    @Override
    public Event getEventById(Integer id) {
        Event event =  eventMapper.getEventById(id);
        if(event == null)
        {
            throw new RecordNotFoundException("你查找的id为" + id+"的活动事件不存在!");
        }
        return event;
    }

    /**
     * 返回所有事件
     * @return
     */
    @Override
    public List<Event> getAllEvents() {
        return eventMapper.getAllEvents();
    }

    /**
     * 创建事件
     * @param event
     */
    @Override
    public void createEvent( Event event) {
        if(event == null)
        {
            throw new IllegalArgumentException("你创建的活动事件信息不能为空！");
        }
        eventMapper.insert(event);

    }
    /**
     * 更新事件
     * @param event
     */
    @Override
    public void updateEvent(Event event) {
        if(event == null)
        {
            throw new IllegalArgumentException("你修改的活动事件信息不能为空！");
        }
        eventMapper.update(event);

    }

    /**
     * 删除事件
     * @param id
     */
    @Override
    @Transactional
    public void deleteEventById(Integer id) {
        Integer rowsAffected = eventMapper.deleteEventById(id);
        if(rowsAffected == 0)
        {
            throw new RecordNotFoundException("你要删除的id为"+id+"的活动事件不存在!");
        }

    }
}
