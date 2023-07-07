package com.cluster.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MailLog对象", description = "邮件日志")
public class MailLog {



    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "消息id")
    private String msgId;

    @ApiModelProperty(value = "注册用户id")
    private Integer uid;

    @ApiModelProperty(value = "状态（0:消息投递中 1:投递成功 2:投递失败）")
    private Integer status;

    @ApiModelProperty(value = "路由键")
    private String routeKey;

    @ApiModelProperty(value = "交换机")
    private String exchange;

    @ApiModelProperty(value = "重试次数")
    private Integer count;

    @ApiModelProperty(value = "重试时间")
    private LocalDateTime tryTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
