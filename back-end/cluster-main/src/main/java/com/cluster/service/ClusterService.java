package com.cluster.service;

import com.cluster.pojo.Cluster;
import com.github.pagehelper.PageInfo;

import java.sql.SQLException;
import java.util.List;

public interface ClusterService {



    /**
     * 获取cluster
     * @param id
     * @return
     */
    Cluster getClusterById(Integer id);

    /**
     * 获取所有cluster
     * @return
     */
    List<Cluster> getAllClusters();

    /**
     * 创建新的cluster
     * @param cluster
     */
    void createCluster(Cluster cluster);

    /**
     * 更新cluster
     * @param cluster
     */
    void updateCluster(Cluster cluster);

    /**
     * 注销cluster
     * @param id
     */
    void deleteClusterById(Integer id);


    /**
     * 当前用户加入有兴趣的cluster
     * @param id
     */
    void joinMember(Integer id) throws SQLException;

    void deleteMember(Integer id) throws SQLException;

    void authenticateMember(Integer cid);


    List<Cluster> viewMyClusters();

    PageInfo<Cluster> getClusterByPage(Integer page, Integer size);





}
