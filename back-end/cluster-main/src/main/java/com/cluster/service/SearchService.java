package com.cluster.service;

import com.cluster.enums.Category;
import com.cluster.pojo.Cluster;
import com.cluster.pojo.User;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.util.List;


public interface SearchService{


    List<User> testSearch(String name);
    PageInfo<User> searchUsersByName(String name, Integer page, Integer pageSize);

    User searchUserByUsername(String name);

    PageInfo<Cluster> searchClusterByName(String clusterName, Integer page, Integer pageSize);

    PageInfo<Cluster> searchClusterByTag(String tag, Integer page, Integer pageSize);

    PageInfo<Cluster> displayCategoryCluster(Category category, Integer page, Integer pageSize);
}
