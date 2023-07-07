package com.cluster.controller;

import com.cluster.exception.RecordNotFoundException;
import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.Cluster;
import com.cluster.pojo.Event;
import com.cluster.service.ClusterService;
import com.cluster.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private ClusterService clusterService;

    @ApiOperation(value = "获取所有的活动事件")
    @GetMapping("/event/list")
    public List<Event> getAllEvents()
    {
        return eventService.getAllEvents();
    }

    @ApiOperation(value = "获取特定的活动事件")
    @GetMapping("/cluster/{id}/event/{eid}/")
    public ApiResponse getEventInfoById(@PathVariable("id")Integer id, @PathVariable("eid")Integer eid)
    {
        try
        {
            clusterService.authenticateMember(id);
            Event event = eventService.getEventInfoById(eid);
            return ApiResponse.success("获取活动事件成功", event);
        }catch(RecordNotFoundException exception)
        {
            exception.printStackTrace();
            return ApiResponse.error("你查找的活动事件不存在");
        }catch(AccessDeniedException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，权限验证失败!");
        }
    }


    /**
     *  这里event对应的clusterId不在后端来处理，而是在前端传过来的requestbody里面获取
     * @param event
     * @return
     */
    @ApiOperation(value = "创建新的活动事件")
    @PostMapping("/cluster/{id}/event/")
    public ApiResponse createEvent(@PathVariable("id") Integer cid, @RequestBody Event event)
    {
        try
        {
            clusterService.authenticateMember(cid);
            eventService.createEvent(cid, event);
            return ApiResponse.success("创建活动事件成功!");
        }catch(Exception e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，权限验证或创建活动事件失败");
        }
    }

    @ApiOperation(value = "更新Event")
    @PutMapping("/cluster/{id}/event")
    public ApiResponse updateEvent(@RequestBody Event event)
    {
        try
        {
            clusterService.authenticateMember(event.getClusterId());
            eventService.updateEvent(event);
            return ApiResponse.success("更新活动事件成功!");
        }catch(IllegalArgumentException e)
        {
            e.printStackTrace();
            return ApiResponse.error("更新活动事件失败");
        }
        catch(AccessDeniedException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，权限验证失败!");
        }

    }
    @ApiOperation(value = "删除对应的活动")
    @DeleteMapping("/cluster/{id}/event/{eid}")
    public ApiResponse deleteEvent(@PathVariable("id")Integer cid, @PathVariable("eid") Integer eid)
    {
        try
        {
            clusterService.authenticateMember(cid);
            eventService.deleteEventById(eid);
            return ApiResponse.success("删除活动事件成功!");
        }catch(RecordNotFoundException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，删除活动事件失败");
        }
        catch(AccessDeniedException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，权限验证失败!");
        }

    }

    @ApiOperation(value = "分页查找events")
    @GetMapping(value = "/event/list/page")
    public ApiResponse getEventByPage(@RequestParam("page")Integer page)
    {
        int pageSize = 8;

        int offset = (page - 1) * pageSize;

        List<Event> events= eventService.getEventByPage(page, pageSize).getList();

        return ApiResponse.success("获取当前页的clusters成功", events);

    }
}
