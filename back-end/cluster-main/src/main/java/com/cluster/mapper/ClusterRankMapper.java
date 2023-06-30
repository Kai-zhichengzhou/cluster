package com.cluster.mapper;

import com.cluster.enums.ClusterRank;

import java.util.List;

public interface ClusterRankMapper {

    /**
     * 获取clusterrank
     * @param id
     * @return
     */
    ClusterRank getClusterRankById(Integer id);

    /**
     * 获取所有clusterrank
     * @return
     */
    List<ClusterRank> getAllClusterRank();

    /**
     * 创建clusterrank
     * @param clusterRank
     */
    void insert(ClusterRank clusterRank);

    /**
     * 更新clusterrank
     * @param clusterRank
     */
    void update(ClusterRank clusterRank);

    /**
     * 删除clusterrank
     * @param id
     */
    Integer deleteById(Integer id);
}
