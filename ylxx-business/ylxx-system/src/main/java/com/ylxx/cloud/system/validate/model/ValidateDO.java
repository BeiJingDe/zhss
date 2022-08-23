package com.ylxx.cloud.system.validate.model;

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
 * 前后端一致校验规则表
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Validate对象", description="前后端一致校验规则表")
@TableName("t_system_validate")
public class ValidateDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotBlank(groups = { ValidateGroup.Update.class }, message = "主键不能为空")
    private String id;

    @ApiModelProperty(value = "校验名称")
    @NotBlank(groups = { ValidateGroup.Update.class, ValidateGroup.Insert.class }, message = "校验名称不能为空")
    private String validatorName;

    @ApiModelProperty(value = "校验类型")
    @NotBlank(groups = { ValidateGroup.Update.class, ValidateGroup.Insert.class }, message = "校验类型不能为空")
    private String type;

    @ApiModelProperty(value = "校验正则")
    @NotBlank(groups = { ValidateGroup.Update.class, ValidateGroup.Insert.class }, message = "校验正则不能为空")
    private String regex;

    @ApiModelProperty(value = "校验提示信息")
    @NotBlank(groups = { ValidateGroup.Update.class, ValidateGroup.Update.class }, message = "校验提示信息不能为空")
    private String message;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
