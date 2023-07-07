package com.cluster.pojo;


import com.cluster.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "User对象", description = "")
public class User implements Serializable , UserDetails {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "是否启用")
    @Getter(AccessLevel.NONE) // 不需要生成 get 方法，防止与 UserDetails 重写的 isEnabled 冲突
    private Boolean enabled;

    @ApiModelProperty(value = "用户昵称")
    private String name;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "注册日期")
    private Date dateJoined;

    @ApiModelProperty(value = "性别")
    private Gender gender;

    @ApiModelProperty(value = "个人简介")
    private String signature;

    @ApiModelProperty(value = "等级积分")
    private Integer rank;

    @ApiModelProperty(value = "用户头像")
    private String avatarPath;

    @ApiModelProperty(value = "权限")
    private Role role;



    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.getName()));
        return authorities;

    }


    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
