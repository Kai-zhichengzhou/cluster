package com.cluster.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "标签对象", description = "")
public class Tag {
    @ApiModelProperty(value = "标签ID")
    private Integer id;
    @ApiModelProperty(value = "标签名称")
    private String tagName;

    /**
     * 多对多关系
     */
    @ApiModelProperty(value = "标注当前标签的cluster列表")
    private List<Cluster> clusterList;


}
