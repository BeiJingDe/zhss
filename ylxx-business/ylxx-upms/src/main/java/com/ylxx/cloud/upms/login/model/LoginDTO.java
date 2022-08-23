package com.ylxx.cloud.upms.login.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="LoginDTO对象", description="用户登录参数对象")
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 方式1：通过本地用户名密码登录
    @ApiModelProperty(value = "本地用户登录名")
    private String username;
    @ApiModelProperty(value = "本地用户密码")
    private String mmpd;

    // 方式2：通过isc返回的ticket登录
    @ApiModelProperty(value = "isc返回的ticket")
    private String ticket;

}