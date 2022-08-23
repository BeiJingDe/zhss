package com.ylxx.cloud.system.permissionurl.model;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * 允许的url名单
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PermissionUrl对象", description="允许的url名单")
@TableName("t_system_permission_url")
public class PermissionUrlDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotBlank(groups = { ValidateGroup.Update.class }, message = "主键不能为空")
    private String id;

    @ApiModelProperty(value = "微服务名称")
    private String appName;

    @ApiModelProperty(value = "请求url")
    @NotBlank(groups = { ValidateGroup.Insert.class,ValidateGroup.Update.class }, message = "请求url不能为空")
    private String reqUrl;

    @ApiModelProperty(value = "请求方式（GET、POST、PUT、DELETE等）")
    private String reqMethod;
    
    @ApiModelProperty(value = "是否生效（0：否；1：是）")
    private String isActive;
    
    @ApiModelProperty(value = "请求url描述")
    @NotBlank(groups = { ValidateGroup.Insert.class,ValidateGroup.Update.class }, message = "请求url描述不能为空")
    private String description;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
