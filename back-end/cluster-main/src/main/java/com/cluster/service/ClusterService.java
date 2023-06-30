package com.cluster.service;

import com.cluster.pojo.Cluster;

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




}
