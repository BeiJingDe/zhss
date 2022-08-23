package com.ylxx.cloud.upms.role.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ylxx.cloud.core.annotation.ValidateModel;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色信息表
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RoleDO对象", description="角色信息表")
@TableName("t_base_role")
public class RoleDO implements Serializable {
	
	// 说明：此model字段属性与数据库字段一一对应，不允许在此处添加数据没有的字段
	
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "主键")
    @NotBlank(groups = {ValidateGroup.Update.class}, message = "主键不能为空")
    private String id;

    @ApiModelProperty(value = "角色名称")
    @NotBlank(groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class}, message = "角色名称不能为空")
    private String roleName;

    @ApiModelProperty(value = "角色编码")
    @NotBlank(groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class}, message = "角色编码不能为空")
    private String roleCode;

    @ApiModelProperty(value = "权限等级（数值越小，权限越大）")
    @NotNull(groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class}, message = "权限等级不能为空")
    private Integer sortNo;

    @ApiModelProperty(value = "角色状态（1：正常；2：停用；3：删除）")
    private String status;

    @ApiModelProperty(value = "备注信息")
    private String remarks;

    @ApiModelProperty(value = "创建用户")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新用户")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
