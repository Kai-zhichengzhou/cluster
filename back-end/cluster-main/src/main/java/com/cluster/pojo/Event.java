package com.cluster.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "事件对象", description = "")
public class Event implements Serializable {

    @ApiModelProperty(value = "事件ID")
    private Integer id;
    @ApiModelProperty(value = "所属clusterID")
    private Integer clusterId;
    @ApiModelProperty(value = "事件名称")
    private String name;
    @ApiModelProperty(value = "事件介绍")
    private String description;
    @ApiModelProperty(value = "开始日期")
    private Date beginDate;
    @ApiModelProperty(value = "结束日期")
    private Date endDate;
    @ApiModelProperty(value = "是否结束")
    private Boolean finished;
    @ApiModelProperty(value = "报名人数")
    private Integer applicantCount;


    /**
     *    多对多关系
     */
    @ApiModelProperty(value = "报名成员")
    private List<User> applicants;


}
