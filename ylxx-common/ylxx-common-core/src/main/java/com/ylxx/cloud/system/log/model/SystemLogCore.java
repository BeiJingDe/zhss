package com.ylxx.cloud.system.log.model;

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
 * 系统日志
 *
 * @author caixiaopeng
 * @since 2020-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SystemLog对象", description="系统日志")
@TableName("t_system_log")
public class SystemLogCore implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "菜单编码")
    private String menuCode;

    @ApiModelProperty(value = "日志模块")
    private String logModule;

    @ApiModelProperty(value = "日志内容")
    private String logContent;

    @ApiModelProperty(value = "操作类型（01：新增；02：删除；03：修改；04：查询；...）")
    private String operateType;

    @ApiModelProperty(value = "操作结果（0：失败；1：成功）")
    private String operateResult;

    @ApiModelProperty(value = "异常详情")
    private String expMessage;

    @ApiModelProperty(value = "请求用户IP")
    private String operateIp;
    
    @ApiModelProperty(value = "微服务名称")
    private String appName;
    
    @ApiModelProperty(value = "微服务部署IP")
    private String appIp;
    
    @ApiModelProperty(value = "微服务部署端口")
    private Integer appPort;

    @ApiModelProperty(value = "请求url")
    private String reqUrl;

    @ApiModelProperty(value = "请求方式（GET、POST、PUT、DELETE等）")
    private String reqMethod;

    @ApiModelProperty(value = "请求参数")
    private String reqParams;
    
    @ApiModelProperty(value = "消耗时间（ms）")
    private Integer timeConsuming;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "日志类型")
    private String logType;

    @ApiModelProperty(value = "日志等级")
    private String logLevel;

}
