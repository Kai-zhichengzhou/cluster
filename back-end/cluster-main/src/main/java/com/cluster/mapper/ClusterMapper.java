package com.cluster.mapper;

import com.cluster.pojo.Cluster;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * cluster相关操作
 */
public interface ClusterMapper {

    /**
     * 获取cluster
     * @param id
     * @return
     */
    Cluster getClusterById(Integer id);

    /**
     * 返回所有cluster
     * @return
     */
    List<Cluster> getAllClusters();

    /**
     * 创建cluster
     * @param cluster
     */
    void insert(Cluster cluster);

    /**
     * 更新cluster
     * @param cluster
     */
    void update(Cluster cluster);

    /**
     * 注销cluster
     * @param id
     */
    Integer deleteClusterById(Integer id);
}
