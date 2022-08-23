package com.ylxx.cloud.upms.org.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 组织信息表
 *
 * @author caixiaopeng
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OrgDO对象", description="组织信息表")
@TableName("t_base_org")
public class OrgDO implements Serializable {
	
	// 说明：此model字段属性与数据库字段一一对应，不允许在此处添加数据库表没有的字段
	
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "主键")
    @NotBlank(groups = {ValidateGroup.Update.class}, message = "主键不能为空")
    private String id;

    @ApiModelProperty(value = "父级主键（最高级默认为：/）")
    private String parentId;

    @ApiModelProperty(value = "组织名称")
    @NotBlank(groups = {ValidateGroup.Update.class,ValidateGroup.Insert.class}, message = "组织名称不能为空")
    private String orgName;

    @ApiModelProperty(value = "组织编码")
    @NotBlank(groups = {ValidateGroup.Update.class,ValidateGroup.Insert.class}, message = "组织编码不能为空")
    private String orgCode;

    @ApiModelProperty(value = "组织类型")
    private String orgType;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "地址")
    private String location;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "状态（1：正常；2：停用；3：删除）")
    private String status;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区/县")
    private String county;

    @ApiModelProperty(value = "排序字段")
    private Integer sortNo;

    @ApiModelProperty(value = "创建用户")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新用户")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
