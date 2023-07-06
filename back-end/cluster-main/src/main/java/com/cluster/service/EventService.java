package com.cluster.service;

import com.cluster.pojo.Event;
import com.github.pagehelper.PageInfo;

import java.sql.SQLException;
import java.util.List;

public interface EventService {

    /**
     * 获取事件
     * @param id
     * @return
     */
    Event getEventInfoById(Integer id);

    /**
     * 获取当前cluster注册的所有事件
     * @param cid
     * @return
     */
    List<Event> getClusterEvents(Integer cid);

    /**
     * 返回所有事件
     * @return
     */
    List<Event> getAllEvents();

    /**
     * 创建事件
     * @param event
     */
    void createEvent(Integer cid, Event event) throws SQLException;

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

    /**
     * 加入报名者
     * @param eid
     * @param eid
     */
    void addEventMember(Integer eid);

    /**
     * 成员退出
     * @param eid
     * @param eid
     */
    void cancelEventMember(Integer eid);

    /**
     * 获取当前用户报名过的所有事件
     * @return
     */
    List<Event> viewMyEvents();


    PageInfo<Event> getEventByPage(Integer page, Integer size);






}
