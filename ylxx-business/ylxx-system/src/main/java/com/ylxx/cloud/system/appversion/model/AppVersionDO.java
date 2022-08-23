package com.ylxx.cloud.system.appversion.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import com.ylxx.cloud.core.annotation.Excel;
import com.ylxx.cloud.core.annotation.Excels;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 手机APP版本管理
 *
 * @author caixiaopeng
 * @since 2021-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AppVersionDO对象", description="手机APP版本管理")
@TableName("t_system_app_version")
public class AppVersionDO implements Serializable {
	
	// 说明：此model字段属性与数据库字段一一对应，不允许在此处添加数据库表没有的字段
	
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "app版本号")
    @NotBlank(groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class}, message = "app版本号不能为空")
    private String appVersion;

    @ApiModelProperty(value = "app安装包文件ID")
    @NotBlank(groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class}, message = "app安装包文件ID不能为空")
    private String fileId;

    @ApiModelProperty(value = "是否发布（0：否；1：是）")
    private String isPublish;

    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "排序字段")
    @NotNull(groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class}, message = "排序字段不能为空")
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
