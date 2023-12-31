package com.cluster.pojo;


import com.cluster.enums.Category;
import com.cluster.enums.ClusterRank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "Cluster对象", description = "")
public class Cluster implements Serializable {

    @ApiModelProperty(value = "clusterId")
    private Integer clusterId;
    @ApiModelProperty(value = "cluster名称")
    private String clusterName;

    @ApiModelProperty(value = "创建者ID")
    private Integer founderId;
    @ApiModelProperty(value = "分类")
    private Category category;
    @ApiModelProperty(value = "当前成员数量")
    private Integer memberCount;
    @ApiModelProperty(value = "成员上限数量")
    private Integer maxMembers;
    @ApiModelProperty(value = "创建日期")
    private Date createdDate;
    @ApiModelProperty(value = "cluster级别")
    private ClusterRank clusterRank;
    @ApiModelProperty(value = "cluster封面")
    private String coverPath;

    private String description;

    //其余一对多，多对多关系
    //一对多
    @ApiModelProperty(value = "cluster发布事件")
    private List<Event> events;
    //多对多
    @ApiModelProperty(value = "成员列表")
    private List<User> members;
    @ApiModelProperty(value = "cluster标签")
    private List<Tag> tags;

//    public Integer getFounderId() {
//        return founderId;
//    }
//    public void setFounderId(Integer founderId) {
//        this.founderId = founderId;
//    }

    @Override
    public String toString() {
        return "Cluster{" +
                "clusterId=" + clusterId +
                ", clusterName='" + clusterName + '\'' +
                ", founderId=" + founderId +
                ", category=" + category +
                ", memberCount=" + memberCount +
                ", maxMembers=" + maxMembers +
                ", createdDate=" + createdDate +
                ", clusterRank=" + clusterRank +
                '}';
    }
}
