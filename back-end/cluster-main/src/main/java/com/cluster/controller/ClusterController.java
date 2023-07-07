package com.cluster.controller;

import com.cluster.exception.RecordNotFoundException;
import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.Cluster;
import com.cluster.pojo.Event;
import com.cluster.service.ClusterService;
import com.cluster.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/cluster")
public class ClusterController {


    @Autowired
    private ClusterService clusterService;
    @Autowired
    private EventService eventService;

    @ApiOperation(value = "获取所有Cluster")
    @GetMapping("/list")
    public List<Cluster> getAllCluster()
    {
        return clusterService.getAllClusters();

    }

    @GetMapping("/{id}")
    public ApiResponse getClusterById(@PathVariable Integer id)
    {
        try
        {
            clusterService.authenticateMember(id);
            Cluster cluster = clusterService.getClusterById(id);
            return ApiResponse.success("查找cluster成功", cluster);
        }catch(RecordNotFoundException exception)
        {
            exception.printStackTrace();
            return ApiResponse.error("你查找的cluster不存在");
        }catch(AccessDeniedException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，权限验证失败!");
        }
    }

    @ApiOperation(value = "创建新的cluster")
    @PostMapping("/")
    public ApiResponse createCluster(@RequestBody Cluster cluster)
    {
        try
        {
            clusterService.createCluster(cluster);
            return ApiResponse.success("创建Cluster成功!");
        }catch(IllegalArgumentException exception)
        {
            exception.printStackTrace();
            return ApiResponse.error("很抱歉，创建Cluster失败");
        }
    }

    @ApiOperation(value = "删除cluster")
    @DeleteMapping("/{id}")
    public ApiResponse deleteCluster(@PathVariable Integer id)
    {
        try
        {
            clusterService.authenticateMember(id);
            clusterService.deleteClusterById(id);
            return ApiResponse.success("删除Cluster成功!");
        }catch(RecordNotFoundException exception)
        {
            exception.printStackTrace();
            return ApiResponse.error("很抱歉，删除Cluster失败");
        }catch(AccessDeniedException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，权限验证失败!");
        }
    }

    @ApiOperation(value = "更新cluster")
    @PutMapping("/")
    public ApiResponse updateCluster(@RequestBody Cluster cluster)
    {
        try
        {
            clusterService.authenticateMember(cluster.getClusterId());
            clusterService.updateCluster(cluster);
            return ApiResponse.success("更新Cluster成功!");
        }catch(IllegalArgumentException e)
        {
            e.printStackTrace();
            return ApiResponse.error("更新Cluster失败");
        }
        catch(AccessDeniedException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，权限验证失败!");
        }

    }

    @ApiOperation(value = "当前用户加入Cluster")
    @PutMapping("/{id}/join")
    public ApiResponse joinCluster(@PathVariable Integer id)
    {
        try
        {
            clusterService.joinMember(id);
            return ApiResponse.success("恭喜你，加入cluster成功!");
        }catch(RecordNotFoundException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，加入cluster失败!请重试或联系客服。");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，加入cluster失败!请重试或联系客服。");
        }

    }

    @ApiOperation(value = "当前用户退出Cluster")
    @DeleteMapping("/{id}/join")
    public ApiResponse existCluster(@PathVariable Integer id)
    {
        try
        {
            clusterService.deleteMember(id);
            return ApiResponse.success("你已成功的退出了当前Cluster,我们江湖再见!");
        }catch(RecordNotFoundException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，退出cluster失败!请重试或联系客服。");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，退出cluster失败!请重试或联系客服。");
        }

    }

    @ApiOperation(value = "获取当前cluster所有的事件")
    @GetMapping("/{id}/events")
    public ApiResponse getAllEvents(@PathVariable Integer id)
    {
        try
        {
            clusterService.authenticateMember(id);
            List<Event> events = eventService.getClusterEvents(id);
            return ApiResponse.success("查找Event成功", events);
        }catch(Exception e)
        {
            e.printStackTrace();
            return ApiResponse.error("很抱歉，获取Event失败，请联系客服!");
        }
    }


    @ApiOperation(value = "分页查找cluster")
    @GetMapping(value = "/list/page")
    public ApiResponse getClusterByPage(@RequestParam("page")Integer page)
    {
        int pageSize = 8;

        int offset = (page - 1) * pageSize;

        List<Cluster> clusters = clusterService.getClusterByPage(page, pageSize).getList();

        return ApiResponse.success("获取当前页的clusters成功", clusters);

    }


}
