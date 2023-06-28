package com.cluster.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Api(value = "LoginRequest")
public class LoginRequest {

    @ApiModelProperty(value = "用户名" ,required = true)
    private String username;
    @ApiModelProperty(value = "密码" ,required = true)
    private String password;
    @ApiModelProperty(value = "验证码" ,required = true)
    private String code;
}
