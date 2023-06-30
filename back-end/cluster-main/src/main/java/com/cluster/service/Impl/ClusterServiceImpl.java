package com.cluster.service.Impl;

import com.cluster.exception.RecordNotFoundException;
import com.cluster.mapper.ClusterMapper;
import com.cluster.pojo.Cluster;
import com.cluster.pojo.User;
import com.cluster.service.ClusterService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ClusterServiceImpl implements ClusterService {

    @Autowired
    private ClusterMapper clusterMapper;

    /**
     * 获取cluster
     * @param id
     * @return
     */
    @Override
    public Cluster getClusterById(Integer id) {
        Cluster cluster =  clusterMapper.getClusterById(id);
        if(cluster == null)
        {
            throw new RecordNotFoundException("你查找的id为" + id+"的cluster不存在!");
        }
        return cluster;

    }

    /**
     * 获取所有cluster
     * @return
     */
    @Override
    public List<Cluster> getAllClusters() {
        return clusterMapper.getAllClusters();
    }

    /**
     * 创建新的cluster
     * @param cluster
     */
    @Override
    @Transactional
    public void createCluster(Cluster cluster) {
        if(cluster == null)
        {
            throw new IllegalArgumentException("你创建的Cluster信息不能为空！");
        }
        cluster.setCreatedDate(new Date());
        cluster.setFounderID(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        clusterMapper.insert(cluster);

    }
    /**
     * 更新cluster
     * @param cluster
     */
    @Override
    @Transactional
    public void updateCluster(Cluster cluster) {
        if(cluster == null)
        {
            throw new IllegalArgumentException("你修改的cluster信息不能为空！");
        }
        clusterMapper.update(cluster);

    }
    /**
     * 注销cluster
     * @param id
     */
    @Override
    @Transactional
    public void deleteClusterById(Integer id) {
        Integer rowsAffected = clusterMapper.deleteClusterById(id);
        if(rowsAffected == 0)
        {
            throw new RecordNotFoundException("你要删除的id为"+id+"的cluster不存在!");
        }


    }
}
