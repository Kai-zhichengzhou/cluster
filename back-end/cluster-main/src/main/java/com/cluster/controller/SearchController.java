package com.cluster.controller;

import com.cluster.enums.Category;
import com.cluster.exception.RecordNotFoundException;
import com.cluster.pojo.ApiResponse;
import com.cluster.pojo.Cluster;
import com.cluster.pojo.User;
import com.cluster.service.ClusterService;
import com.cluster.service.SearchService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 定义一系列的搜索逻辑 - Controller层
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    public static final int pageSize = 8;

    @Autowired
    private SearchService searchService;

    @Autowired
    private ClusterService clusterService;


    /**
     * 基于模糊查询来返回所有与name能匹配的用户信息
     * 注意，在前端实现的时候，从搜索框搜索的第一次都会传入page为1
     * TODO: 实现有精准匹配的user显示在列表的前排
     * @return
     */
    @GetMapping("/users/{name}")
    public ApiResponse searchUsersByName(@PathVariable String name, @RequestParam("page")Integer page)
    {

        System.out.println(name);
//        List<User> users = searchService.searchUsersByName(name,page, pageSize).getList();
        List<User> users = searchService.testSearch(name);
        System.out.println(users.size());

        return ApiResponse.success("获取你搜索的用户当前页成功", users);

    }

    /**
     * 根据username精准搜索用户
     * @param username
     * @return
     */
    @GetMapping("/user/{username}")
    public ApiResponse searchUserByUsername(@PathVariable String username)
    {
        User user =  searchService.searchUserByUsername(username);
        if(user == null) return ApiResponse.error("你搜索的用户不存在");

        return ApiResponse.success("搜索用户成功", user);

    }

    /**
     * 基于模糊查询来返回所有能与name匹配的cluster信息
     * @param clusterName
     * @return
     */
    @GetMapping("/cluster/")
    public ApiResponse searchClusterByName(@RequestParam("clusterName") String clusterName, @RequestParam("page") Integer page)
    {
            List<Cluster> clusters = searchService.searchClusterByName(clusterName, page, pageSize).getList();
            return ApiResponse.success("获取你搜索的Cluster当前页成功", clusters);
    }

    /**
     * 精准搜索想要查询/加入的cluster
     * @param clusterId
     * @return
     */
    @GetMapping("/cluster/{id}")
    public ApiResponse searchClusterByClusterId(@PathVariable("id") Integer clusterId)
    {
        try {
            Cluster cluster = clusterService.getClusterById(clusterId);
            return ApiResponse.success("搜索Cluster成功", cluster);

        }catch(RecordNotFoundException e)
        {
            return ApiResponse.error("你查找的Cluster不存在");
        }

    }

    /**
     * 根据用户输入的标签来查找所有设定了该标签的cluster
     * @param tag
     * @return
     */
    @GetMapping("/cluster/tag")
    public ApiResponse searchClusterByTag(@RequestParam String tag, @RequestParam("page") Integer page)
    {
            List<Cluster> clusters = searchService.searchClusterByTag(tag, page, pageSize).getList();
            return ApiResponse.success("获取属于当前Tag标签的Cluster成功", clusters);

    }

    /**
     * 根据应用定义的八大分类，来展示所有的cluster
     * @param categoryStr
     * @return
     */
    @GetMapping("/cluster/category/{categoryStr}")
    public ApiResponse displayCategoryCluster(@PathVariable String categoryStr, @RequestParam("page") Integer page)
    {
        try
        {
            Category category = Category.valueOf(categoryStr.toUpperCase());
            List<Cluster> clusters = searchService.displayCategoryCluster(category, page, pageSize).getList();
            return ApiResponse.success("获取属于当前分类的Cluster成功", clusters);

        }catch (IllegalArgumentException e)
        {
            return ApiResponse.error("分类错误");
        }
    }




}
