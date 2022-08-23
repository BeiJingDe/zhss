package com.ylxx.cloud.system.config.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 系统参数
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ConfigDO对象", description="系统参数")
@TableName("t_system_config")
public class ConfigDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotBlank(groups = { ValidateGroup.Update.class }, message = "主键ID不能为空")
    private String id;

    @ApiModelProperty(value = "配置分组")
    @NotBlank(groups = { ValidateGroup.Insert.class }, message = "配置分组不能为空")
    private String configGroup;

    @ApiModelProperty(value = "配置名称")
    @NotBlank(groups = { ValidateGroup.Insert.class }, message = "配置名称不能为空")
    @TableField("`key`")
    private String key;

    @ApiModelProperty(value = "配置参数值")
    @NotBlank(groups = { ValidateGroup.Insert.class, ValidateGroup.Update.class }, message = "配置参数值不能为空")
    private String value;

    @ApiModelProperty(value = "配置描述")
    @NotBlank(groups = { ValidateGroup.Insert.class }, message = "配置描述不能为空")
    private String description;

    @ApiModelProperty(value = "是否加密（0：否；1：是）")
    private String isEncrypt;

    @ApiModelProperty(value = "排序字段")
    private Integer sortNo;

    @ApiModelProperty(value = "是否展示（0：否；1：是）")
    private String isShow;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
