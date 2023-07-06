package com.cluster.mapper;

import com.cluster.pojo.Event;
import org.apache.ibatis.annotations.Param;

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
     * 获取当前cluster下的所有事件
     * @param cid
     * @return
     */
    List<Event> getClusterEvents(Integer cid);

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

    /**
     * 为当前的事件添加报名人
     * @param eid
     * @param uid
     * @return
     */
    Integer addEventMember(@Param("eid") Integer eid, @Param("uid") Integer uid);

    /**
     * 为当前的事件删除报名人
     * @param eid
     * @param uid
     * @return
     */
    Integer cancelEventMember(@Param("eid") Integer eid, @Param("uid") Integer uid);

    /**
     * 查找中间表，获取匹配uid的所有events
     * @param uid
     * @return
     */
    List<Integer> findEventIdByUser(Integer uid);

    /**
     * 查找当前登陆用户报名过的所有事件
     * @param eids
     * @return
     */
    List<Event> viewMyEvents(List<Integer> eids);


}
