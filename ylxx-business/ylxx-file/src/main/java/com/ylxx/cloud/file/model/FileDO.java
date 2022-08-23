package com.ylxx.cloud.file.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ylxx.cloud.core.annotation.ValidateModel;

import javax.validation.constraints.NotBlank;

/**
 * 文件管理表
 *
 * @author caixiaopeng
 * @since 2020-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FileDO对象", description="文件管理表")
@TableName("t_base_file")
@ValidateModel
public class FileDO implements Serializable {
	
	// 说明：此model字段属性与数据库字段一一对应，不允许在此处添加数据没有的字段
	
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件ID")
    private String id;

    @ApiModelProperty(value = "所属对象主键")
    private String belongId;

    @ApiModelProperty(value = "文件原始名称")
    private String fileName;

    @ApiModelProperty(value = "文件存储路径")
    private String filePath;

    @ApiModelProperty(value = "文件状态（0：已删除；1：正常）")
    private String status;

    @ApiModelProperty(value = "扩展属性信息")
    private String extraProp;

    @ApiModelProperty(value = "备注信息")
    private String remarks;

    @ApiModelProperty(value = "下载次数")
    private Integer downloadCount;

    @ApiModelProperty(value = "创建用户")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新用户")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "文件类型")
    private String fileType;


}
