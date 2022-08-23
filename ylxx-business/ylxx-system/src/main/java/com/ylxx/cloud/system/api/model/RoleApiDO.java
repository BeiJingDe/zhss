package com.ylxx.cloud.system.api.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import com.ylxx.cloud.core.annotation.Excel;
import com.ylxx.cloud.core.annotation.Excels;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色API接口权限表
 *
 * @author caixiaopeng
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RoleApiDO对象", description="角色API接口权限表")
@TableName("t_system_role_api")
public class RoleApiDO implements Serializable {
	
	// 说明：此model字段属性与数据库字段一一对应，不允许在此处添加数据库表没有的字段
	
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "微服务名称")
    private String appName;

    @ApiModelProperty(value = "请求url")
    private String reqUrl;

    @ApiModelProperty(value = "请求方式(GET、POST、PUT、DELETE等)")
    private String reqMethod;

    @ApiModelProperty(value = "是否生效(0=否,1=是)")
    private String isActive;

    @ApiModelProperty(value = "请求url描述")
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
