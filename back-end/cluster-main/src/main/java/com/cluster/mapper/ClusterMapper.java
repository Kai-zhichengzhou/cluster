package com.cluster.mapper;

import com.cluster.pojo.Cluster;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

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

    /**
     * 为cluster添加成员，包含创建者自己
     * @param cid
     * @param uid
     */
    Integer joinMember(@Param("cid") Integer cid, @Param("uid") Integer uid);

    /**
     * 当前用户退出cluster
     * @param cid
     * @param uid
     * @return
     */
    Integer deleteMember(@Param("cid") Integer cid, @Param("uid") Integer uid);
    /**
     * 更新cluster当前成员数量
     * @param memberCount
     * @param id
     */
    void updateClusterMemberCount(@Param("memberCount") Integer memberCount, @Param("clusterId") Integer id);

    /**
     * 更新Cluster当前成员上限
     * @param maxMembers
     * @param id
     */
    void updateMaxMembers(@Param("maxMembers") Integer maxMembers, @Param("clusterId") Integer id);

    /**
     * 权限验证，查找当前用户是否在该cluster中
     * @param cid
     * @param uid
     * @return
     */
    Map<String, Object> authenticateMember(@Param("cid") Integer cid, @Param("uid") Integer uid);

    /**
     * 通过当前登陆的用户ID来查找他加入的clusters
     * @param uid
     * @return
     */
    List<Integer> findClusterIdByUser(Integer uid);
    List<Cluster> viewMyClusters(List<Integer> cids);
}
