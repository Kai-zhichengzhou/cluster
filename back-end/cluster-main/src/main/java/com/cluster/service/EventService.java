package com.cluster.service;

import com.cluster.pojo.Event;

import java.util.List;

public interface EventService {

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
    void createEvent(Event event);

    /**
     * 更新事件
     * @param event
     */
    void updateEvent(Event event);

    /**
     * 删除事件
     * @param id
     */
    void deleteEventById(Integer id);






}
