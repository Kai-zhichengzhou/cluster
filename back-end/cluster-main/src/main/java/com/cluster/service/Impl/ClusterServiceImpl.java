package com.cluster.service.Impl;

import com.cluster.enums.Category;
import com.cluster.exception.ClusterFullException;
import com.cluster.exception.GeneralClusterException;
import com.cluster.exception.RecordNotFoundException;
import com.cluster.mapper.CategoryMapper;
import com.cluster.mapper.ClusterMapper;
import com.cluster.mapper.TagMapper;
import com.cluster.mapper.UserMapper;
import com.cluster.pojo.Cluster;
import com.cluster.pojo.Tag;
import com.cluster.pojo.User;
import com.cluster.service.ClusterService;
import com.cluster.service.PaginationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ClusterServiceImpl implements ClusterService {

    @Autowired
    private ClusterMapper clusterMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private PaginationService paginationService;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${server.url}")
    String serverUrl;

    @Value("${cluster.cover.path}")
    private String location;


    /**
     * 获取cluster
     *
     * @param id
     * @return
     */
    @Override
    public Cluster getClusterById(Integer id) {
        Cluster cluster = (Cluster) redisTemplate.opsForValue().get("cluster:"+id);
        if(cluster == null)
        {
            cluster =  clusterMapper.getClusterById(id);
            System.out.println(cluster.toString());
            if (cluster == null) {
                throw new RecordNotFoundException("你查找的id为" + id + "的cluster不存在!");
            }
            redisTemplate.opsForValue().set("cluster:" + cluster.getClusterId(), cluster);
        }
        return cluster;

    }

    /**
     * 获取所有cluster
     *
     * @return
     */
    @Override
    public List<Cluster> getAllClusters() {
        return clusterMapper.getAllClusters();
    }

    /**
     * 创建新的cluster
     *
     * @param cluster
     */
    @Override
    @Transactional
    public void createCluster(Cluster cluster) {
        if (cluster == null) {
            throw new IllegalArgumentException("你创建的Cluster信息不能为空！");
        }
        //1. 设置cluster的基本信息，更新cluster所属分类的信息
        cluster.setCreatedDate(new Date());
        Integer founderID = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        cluster.setFounderId(founderID);
        clusterMapper.insert(cluster);
        Category category = cluster.getCategory();
        Integer currCount = categoryMapper.getClusterCountByCategory(category);
        //要使用乐观锁，通过版本号来更新
        categoryMapper.updateCount(category, currCount + 1);
        //2. 通过founderID来更新cluster_user的中间表
        clusterMapper.joinMember(cluster.getClusterId(), founderID);
        //如果cluster没有自己设立的tag，则直接可以返回
        if (cluster.getTags() == null || cluster.getTags().isEmpty()) return;
        //3. 通过创建时创建者使用的或新创建的tag来更新cluster_tag中间表
        //分步：先查询tag是否存在，如果存在则获取tag的id，用来之后更新cluster_tag的中间表
        //如果tag不存在，则通过tagmapper创建一个新的标签，获取新tag的id，然后更新中间表
        //cluster会有多个标签，所以要通过loop list来依次操作
        for (Tag currTag : cluster.getTags()) {
            Tag tag = tagMapper.getTagByName(currTag.getTagName());
            Integer tagId = null;
            if (tag == null) {
                //说明要创建新的标签
                tagMapper.insert(currTag);
                tagId = currTag.getId();
            } else tagId = tag.getId();
            //更新中间表
            tagMapper.updateClusterTag(cluster.getClusterId(), tagId);

        }
        //4.将创建者用户的rank分数加5分
        userMapper.addRankPoint(founderID, userMapper.getUserRank(founderID) + 5);

    }

    /**
     * /**
     * 当前用户加入有兴趣的cluster
     *
     * @param id
     */
    @Override
    @Transactional
    public void joinMember(Integer id) throws SQLException {
        Cluster cluster = clusterMapper.getClusterById(id);
        if (cluster == null) {
            throw new RecordNotFoundException("你要加入的id为" + id + "的cluster不存在!");
        }
        if (cluster.getMemberCount() == cluster.getMaxMembers()) {
            //说明当前cluster人数满了，不可以再加入新的成员
            throw new ClusterFullException("抱歉，当前Cluster有点太火爆了，请再看看还未满员的Cluster:(");
        }

        redisTemplate.delete("cluster:" + id);
        //获取当前security上下文中用户的id
        //然后更新当前用户的rank积分
        User user = userMapper.getUserById(( (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        //不能重复加入
        Map<String, Object> res = clusterMapper.authenticateMember(id,user.getId());
        if(res != null) return;
        System.out.println("---" + user);
        System.out.println(user.getRank());
        userMapper.addRankPoint(user.getId(), user.getRank() + 2);
        //更新user和cluster的中间表，来表示用户成功加入了cluster
        Integer success = clusterMapper.joinMember(id, user.getId());
        if (success == 0) {
            throw new SQLException("很抱歉，加入cluster发生了错误，请联系客服!");
        }

        //同时最后要记得更新cluster的人数
        clusterMapper.updateClusterMemberCount(cluster.getMemberCount() + 1, id);

    }

    /**
     * /**
     * 当前用户退出不再感兴趣的cluster
     * 注意：创建者自己不能退出该cluster
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteMember(Integer id) throws SQLException {

        Cluster cluster = clusterMapper.getClusterById(id);
        if (cluster == null) {
            throw new RecordNotFoundException("你要退出的id为" + id + "的cluster不存在!");
        }
        //获取当前security上下文中用户的id
        //然后更新当前用户的rank积分
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (user.getId().equals(cluster.getFounderId())) {
            throw new GeneralClusterException("Cluster星主不能退出自己创建的cluster");
        }
        Map<String, Object> res = clusterMapper.authenticateMember(id,user.getId());
        if(res == null) return;
        userMapper.addRankPoint(user.getId(), user.getRank() - 1);
        //更新user和cluster的中间表，来表示用户成功加入了cluster
        Integer success = clusterMapper.deleteMember(id, user.getId());
        if (success == 0) {
            throw new SQLException("很抱歉，退出cluster发生了错误，请联系客服!");
        }
        redisTemplate.delete("cluster:" + id);
        //同时最后要记得更新cluster的人数
        clusterMapper.updateClusterMemberCount(cluster.getMemberCount() - 1, id);
    }

    /**
     * 更新cluster
     *
     * @param cluster
     */
    @Override
    @Transactional
    public void updateCluster(Cluster cluster) {
        redisTemplate.delete("cluster:" + cluster.getClusterId());
        if (cluster == null) {
            throw new IllegalArgumentException("你修改的cluster信息不能为空！");
        }
        redisTemplate.delete("cluster:" + cluster.getClusterId());
        clusterMapper.update(cluster);
    }

    /**
     * 注销cluster
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteClusterById(Integer id) {
        Integer rowsAffected = clusterMapper.deleteClusterById(id);
        if (rowsAffected == 0) {
            throw new RecordNotFoundException("你要删除的id为" + id + "的cluster不存在!");
        }
        redisTemplate.delete("cluster:" + id);


    }

    @Override
    public void authenticateMember(Integer cid) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        System.out.println(uid);
        Map<String, Object> res = clusterMapper.authenticateMember(cid, uid);
        if (res == null || res.isEmpty()) {
            System.out.println("it runs here");
            throw new AccessDeniedException("很抱歉，你没有进入该cluster的权限!");

        }
    }

    @Override
    public List<Cluster> viewMyClusters(Integer page, Integer pageSize) {
        Integer uid = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<Integer> cids = clusterMapper.findClusterIdByUser(uid);
//        List<Cluster> myClusters = (cids.isEmpty()) ? null : clusterMapper.searchClusters(cids);
        PageInfo<Cluster> pageInfo = paginationService.getEntityByPage(page, pageSize, ()->clusterMapper.searchClusters(cids));
        return pageInfo.getList();

    }

    @Override
    public PageInfo<Cluster> getClusterByPage(Integer page, Integer pageSize) {

       PageInfo<Cluster> pageInfo = paginationService.getEntityByPage(page, pageSize, ()-> clusterMapper.getAllClusters());
//        System.out.println("page:" + page + " pageSize:" + pageSize);
//        PageHelper.startPage(page, pageSize);
//        List<Cluster> clusters = clusterMapper.getAllClusters();
//        System.out.println(clusters.size());
//        PageInfo<Cluster> pageInfo = new PageInfo<>(clusters);
//        System.out.println(pageInfo.getList().size());
//        Page<Cluster> pageInfo = PageHelper.startPage(page, pageSize).doSelectPage(() -> clusterMapper.getAllClusters());
        return pageInfo;


    }

    @Override
    public List<User> getClusterMember(Integer id) {
       return  clusterMapper.getClusterMember(id);
    }

    @Override
    public void uploadCover(Integer id, String cover) {
        redisTemplate.delete("cluster:" + id);
        clusterMapper.uploadCover(id, cover);
    }


    @Override
    public String getFullCoverUrl(String coverPath) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl)
                .path("/cover/uploads/")
                .pathSegment(coverPath);

        return builder.toUriString();
    }
}


