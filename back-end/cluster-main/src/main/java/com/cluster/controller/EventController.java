package com.cluster.controller;

import com.cluster.exception.RecordNotFoundException;
import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.Event;
import com.cluster.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @ApiOperation(value = "获取所有的活动事件")
    @GetMapping("/list")
    public List<Event> getAllEvents()
    {
        return eventService.getAllEvents();
    }

    @ApiOperation(value = "获取特定的活动事件")
    @GetMapping("/{id}")
    public ApiResponse getEventById(@PathVariable Integer id)
    {
        try
        {
            Event event = eventService.getEventById(id);
            return ApiResponse.success("查找活动事件成功", event);
        }catch(RecordNotFoundException exception)
        {
            exception.printStackTrace();
            return ApiResponse.error("你查找的活动事件不存在");
        }
    }

    /**
     *  这里event对应的clusterId不在后端来处理，而是在前端传过来的requestbody里面获取
     * @param event
     * @return
     */
    @ApiOperation(value = "创建新的活动事件")
    @PostMapping("/")
    public ApiResponse createEvent(@RequestBody Event event)
    {
        try
        {
            eventService.createEvent(event);
            return ApiResponse.success("创建活动事件成功!");
        }catch(IllegalArgumentException exception)
        {
            exception.printStackTrace();
            return ApiResponse.error("很抱歉，创建活动事件失败");
        }
    }

    @ApiOperation(value = "更新Event")
    @PutMapping("/")
    public ApiResponse updateEvent(@RequestBody Event event)
    {
        try
        {
            eventService.updateEvent(event);
            return ApiResponse.success("更新活动事件成功!");
        }catch(IllegalArgumentException e)
        {
            e.printStackTrace();
            return ApiResponse.error("更新活动事件失败");
        }

    }
    @ApiOperation(value = "删除对应的活动")
    @DeleteMapping("/{id}")
    public ApiResponse deleteEvent(@PathVariable Integer id)
    {
        try
        {
            eventService.deleteEventById(id);
            return ApiResponse.success("删除活动事件成功!");
        }catch(RecordNotFoundException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，删除活动事件失败");
        }

    }
}
