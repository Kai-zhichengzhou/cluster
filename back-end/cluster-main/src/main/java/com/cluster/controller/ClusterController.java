package com.cluster.controller;

import com.cluster.exception.RecordNotFoundException;
import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.Cluster;
import com.cluster.service.ClusterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cluster")
public class ClusterController {


    @Autowired
    private ClusterService clusterService;

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
            Cluster cluster = clusterService.getClusterById(id);
            return ApiResponse.success("查找cluster成功", cluster);
        }catch(RecordNotFoundException exception)
        {
            exception.printStackTrace();
            return ApiResponse.error("你查找的cluster不存在");
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
            clusterService.deleteClusterById(id);
            return ApiResponse.success("删除Cluster成功!");
        }catch(RecordNotFoundException exception)
        {
            exception.printStackTrace();
            return ApiResponse.error("很抱歉，删除Cluster失败");
        }
    }

    @ApiOperation(value = "更新cluster")
    @PutMapping("/")
    public ApiResponse updateCluster(@RequestBody Cluster cluster)
    {
        try
        {
            clusterService.updateCluster(cluster);
            return ApiResponse.success("更新Cluster成功!");
        }catch(IllegalArgumentException e)
        {
            e.printStackTrace();
            return ApiResponse.error("更新Cluster失败");
        }

    }


}
