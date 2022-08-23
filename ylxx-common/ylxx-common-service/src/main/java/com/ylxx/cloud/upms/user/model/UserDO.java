package com.ylxx.cloud.upms.user.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ylxx.cloud.core.annotation.Excel;
import com.ylxx.cloud.core.annotation.ValidateModel;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UserDO对象", description="用户信息表")
@TableName("t_base_user")
public class UserDO implements Serializable {
	
	// 说明：此model字段属性与数据库字段一一对应，不允许在此处添加数据没有的字段
	
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "主键")
    @NotBlank(groups = {ValidateGroup.Update.class}, message = "主键ID不能为空")
//    @Excel(name = "主键", sort = 1)
    private String id;

    @ApiModelProperty(value = "用户名")
    @NotBlank(groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class}, message = "用户名不能为空")
    @Excel(name = "用户名", sort = 2)
    private String username;

    @ApiModelProperty(value = "用户真实名字")
    @NotBlank(groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class}, message = "用户真实名字不能为空")
    @Excel(name = "用户真实名字", sort = 3)
    private String realname;

    @ApiModelProperty(value = "密码")
    @NotBlank(groups = {ValidateGroup.Insert.class}, message = "密码不能为空")
    @Excel(name = "密码", sort = 4)
    private String mmpd;

    @ApiModelProperty(value = "性别（0：未知；1：男；2：女）")
    @Excel(name = "性别", sort = 5, dictType = "GENDER", combo = "未知,男,女")
    private String gender;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", sort = 6, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    @ApiModelProperty(value = "电话号码")
    @Excel(name = "电话号码", sort = 7)
    private String phone;

    @ApiModelProperty(value = "邮箱地址")
    @Excel(name = "邮箱地址", sort = 8)
    private String email;

    @ApiModelProperty(value = "头像路径")
//    @Excel(name = "头像路径", sort = 9)
    private String avaterUrl;

    @ApiModelProperty(value = "账号状态（1：正常；2：停用；3：删除）")
//    @Excel(name = "账号状态", sort = 10)
    private String status;

    @ApiModelProperty(value = "省")
//    @Excel(name = "省", sort = 11)
    private String province;

    @ApiModelProperty(value = "市")
//    @Excel(name = "市", sort = 12)
    private String city;

    @ApiModelProperty(value = "区/县")
//    @Excel(name = "区/县", sort = 13)
    private String county;

    @ApiModelProperty(value = "最近一次登录时间")
//    @Excel(name = "最近一次登录时间", sort = 14)
    private Date lastLoginTime;

    @ApiModelProperty(value = "最近一次注销时间")
//    @Excel(name = "最近一次注销时间", sort = 15)
    private Date lastLogoutTime;

    @ApiModelProperty(value = "备注信息")
    @Excel(name = "备注信息", sort = 16)
    private String remarks;

    @ApiModelProperty(value = "创建用户")
//    @Excel(name = "创建用户", sort = 17)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
//    @Excel(name = "创建时间", sort = 18)
    private Date createTime;

    @ApiModelProperty(value = "更新用户")
//    @Excel(name = "更新用户", sort = 19)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
//    @Excel(name = "更新时间", sort = 20)
    private Date updateTime;

}
