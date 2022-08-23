package com.ylxx.cloud.file.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.ylxx.cloud.core.annotation.ValidateModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 文件管理表
 *
 * @author caixiaopeng
 * @since 2020-07-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="FileVO对象", description="文件管理表")
@ValidateModel
public class FileVO extends FileDO {

	// 说明：此model继承了{superEntityClass}，为数据库分页查询selectPageVo返回对象，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;

    @JSONField(serialize = false)
    @ApiModelProperty(value = "文件base64字符串")
    private String base64;
    
}
