package com.cluster.mapper;

import com.cluster.pojo.Event;

import java.util.List;

public interface EventMapper {
    /**
     * 获取事件
     * @param id
     * @return
     */
    Event getEventById(Integer id);

    /**
     * 返回所有事件
     * @return
     */
    List<Event> getAllEvents();

    /**
     * 创建事件
     * @param event
     */
    void insert(Event event);

    /**
     * 更新事件
     * @param event
     */
    void update(Event event);

    /**
     * 删除事件
     * @param id
     */
    Integer deleteEventById(Integer id);

}
