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
 * 系统API接口管理表
 *
 * @author caixiaopeng
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SystemApiDO对象", description="系统API接口管理表")
@TableName("t_system_api")
public class SystemApiDO implements Serializable {
	
	// 说明：此model字段属性与数据库字段一一对应，不允许在此处添加数据库表没有的字段
	
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "微服务名称")
    private String appName;

    @ApiModelProperty(value = "请求url")
    private String reqUrl;

    @ApiModelProperty(value = "请求方式（GET、POST、PUT、DELETE等）")
    private String reqMethod;

    @ApiModelProperty(value = "接口所属模块")
    private String module;

    @ApiModelProperty(value = "接口简要描述")
    private String summary;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
