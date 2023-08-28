package com.cluster.service.Impl;

import com.cluster.enums.Category;
import com.cluster.exception.RecordNotFoundException;
import com.cluster.mapper.ClusterMapper;
import com.cluster.mapper.TagMapper;
import com.cluster.mapper.UserMapper;
import com.cluster.pojo.Cluster;
import com.cluster.pojo.Tag;
import com.cluster.pojo.User;
import com.cluster.service.PaginationService;
import com.cluster.service.SearchService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定义一系列的搜索逻辑
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClusterMapper clusterMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private PaginationService paginationService;

    @Override
    public PageInfo<User> searchUsersByName(String name, Integer page, Integer pageSize) {

        PageInfo<User> pageInfo = paginationService.getEntityByPage(page, pageSize, () -> userMapper.searchUserInfo(name));
        System.out.println(pageSize);

        return pageInfo;

    }

    @Override
    public List<User> testSearch(String name)
    {
        List<User> users = userMapper.searchUserInfo(name);
        for(User user :users)
        {
            System.out.println(user);
        }
        return users;
    }

    @Override
    public User searchUserByUsername(String username) {
        return userMapper.searchUserInfoByUsername(username);
    }

    @Override
    public PageInfo<Cluster> searchClusterByName(String clusterName, Integer page, Integer pageSize) {
        PageInfo<Cluster> pageInfo = paginationService.getEntityByPage(page, pageSize, () -> clusterMapper.searchClusterByName(clusterName));
        return pageInfo;
    }

    @Override
    public PageInfo<Cluster> searchClusterByTag(String tagName, Integer page, Integer pageSize) {
        Tag tag = tagMapper.getTagByName(tagName);
        if(tag == null)
        {
            throw new RecordNotFoundException("你要搜索的标签不存在!");
        }
        List<Integer> cids = clusterMapper.findClusterIdByTag(tag.getId());
        PageInfo<Cluster> pageInfo = paginationService.getEntityByPage(page, pageSize, ()-> clusterMapper.searchClusters(cids));
        return pageInfo;

    }

    @Override
    public PageInfo<Cluster> displayCategoryCluster(Category category, Integer page, Integer pageSize) {

        PageInfo<Cluster> pageInfo = paginationService.getEntityByPage(page, pageSize, ()-> clusterMapper.searchClustersByCategory(category));
        return pageInfo;

    }

}
