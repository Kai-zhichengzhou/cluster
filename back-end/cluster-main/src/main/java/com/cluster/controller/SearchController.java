package com.cluster.controller;

import com.cluster.pojo.Cluster;
import com.cluster.pojo.User;
import com.cluster.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 定义一系列的搜索逻辑 - Controller层
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;


    /**
     * 基于模糊查询来返回所有与name能匹配的用户信息
     * TODO: 实现有精准匹配的user显示在列表的前排
     * @return
     */
    @GetMapping("/users/{name}")
    public List<User> searchUsersByName(@PathVariable String name)
    {

    }

    /**
     * 根据username精准搜索用户
     * @param username
     * @return
     */
    @GetMapping("/user/{username}")
    public User searchUserByUsername(@PathVariable String username)
    {

    }

    /**
     * 基于模糊查询来返回所有能与name匹配的cluster信息
     * @param clusterName
     * @return
     */
    @GetMapping("/cluster/{name}")
    public List<Cluster> searchClusterByName(@PathVariable("name") String clusterName)
    {

    }

    /**
     * 精准搜索想要查询/加入的cluster
     * @param clusterId
     * @return
     */
    @GetMapping("/cluster/{id}")
    public Cluster searchClusterByClusterId(@PathVariable("id") Integer clusterId)
    {

    }

    /**
     * 根据用户输入的标签来查找所有设定了该标签的cluster
     * @param tag
     * @return
     */
    @GetMapping("/cluster/")
    public List<Cluster> searchClusterByTag(@RequestParam String tag)
    {

    }

    /**
     * 根据应用定义的八大分类，来展示所有的cluster
     * @param category
     * @return
     */
    @GetMapping("/cluster/{category}")
    public List<Cluster> displayCategoryCluster(@PathVariable String category)
    {

    }




}
