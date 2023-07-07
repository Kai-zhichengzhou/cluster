package com.cluster.service.Impl;

import com.cluster.exception.RecordNotFoundException;
import com.cluster.mapper.ClusterMapper;
import com.cluster.mapper.EventMapper;
import com.cluster.pojo.Cluster;
import com.cluster.pojo.Event;
import com.cluster.pojo.User;
import com.cluster.service.ClusterService;
import com.cluster.service.EventService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ClusterService clusterService;



    @Override
    public List<Event> getClusterEvents(Integer cid) {
        return eventMapper.getClusterEvents(cid);
    }

    /**
     * 获取事件
     * 逻辑：
     * 首先获取事件是有前提，用户只可以获取或者查看他加入过的cluster的事件
     * 所以在服务层要做一个权限的验证
     * @param id
     * @return
     */
    @Override
    public Event getEventInfoById(Integer id) {
        //先获取事件的条目
        Event event =  eventMapper.getEventById(id);
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
     * 逻辑：
     * 首先设置event的clusterID为当前页面的cluster的ID
     * 设置applicantcount为1，并且要更新event和user的中间表
     *
     * @param event
     */
    @Override
    @Transactional
    public void createEvent(Integer cid,  Event event) throws SQLException {
        if(event == null)
        {
            throw new IllegalArgumentException("你创建的活动事件信息不能为空！");
        }
        event.setApplicantCount(1);
        event.setClusterId(cid);
        eventMapper.insert(event);
        //如果成功插入了event的基本信息，开始更新中间表
        Integer creatorId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        eventMapper.addEventMember(event.getId(), creatorId);

    }
    /**
     * 更新事件
     * 权限验证：
     * 查看当前用户是否为cluster成员，只有通过权限验证才可以进行修改
     * 这里默认前端传来的event包含了要修改的eventid
     * @param event
     */
    @Override
    @Transactional
    public void updateEvent(Event event) {
        eventMapper.update(event);
    }

    /**
     * 删除事件
     * 也是通过封装好的权限验证来处理
     * @param id
     */
    @Override
    @Transactional
    public void deleteEventById(Integer id) {
        eventMapper.deleteEventById(id);

    }

    @Override
    @Transactional
    public void addEventMember(Integer eid) {

        eventMapper.addEventMember(eid, ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

    }

    @Override
    @Transactional
    public void cancelEventMember(Integer eid) {

        eventMapper.cancelEventMember(eid, ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

    }

    @Override
    public List<Event> viewMyEvents()
    {
        Integer uid =  ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<Integer> eids = eventMapper.findEventIdByUser(uid);
        System.out.println(eids.size());
        List<Event> myEvents = (eids.isEmpty()) ? null :eventMapper.viewMyEvents(eids);
        System.out.println(myEvents.size());
        return myEvents;

    }

    @Override
    public PageInfo<Event> getEventByPage(Integer page,  Integer size) {

        PageHelper.startPage(page, size);

        List<Event> events = eventMapper.getAllEvents();

        PageInfo<Event> pageInfo = new PageInfo<>(events);
        //处理分页逻辑
        //如果当前要查询的页面已经超出了总数据量，返回空list
        if (page > pageInfo.getPages() && pageInfo.getPages() != 0) {
            pageInfo.setList(Collections.emptyList());
        }
        return pageInfo;

    }
}
