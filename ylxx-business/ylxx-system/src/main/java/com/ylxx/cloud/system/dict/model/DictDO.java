package com.ylxx.cloud.system.dict.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ylxx.cloud.core.annotation.Excel;
import com.ylxx.cloud.core.annotation.Excels;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Dict对象", description="数据字典")
@TableName("t_system_dict")
public class DictDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotBlank(groups = { ValidateGroup.Update.class }, message = "主键ID不能为空")
    private String id;

    @ApiModelProperty(value = "父主键")
    private String parentId;

    @ApiModelProperty(value = "字典编码值")
    @NotBlank(groups = { ValidateGroup.Insert.class, ValidateGroup.Update.class }, message = "字典编码值不能为空")
    @Excels({
            @Excel(name = "字典编码", sort = 2, groups = 1),
            @Excel(name = "枚举编码", sort = 2, groups = 2)
    })
    private String code;

    @ApiModelProperty(value = "字典显示名称")
    @NotBlank(groups = { ValidateGroup.Insert.class, ValidateGroup.Update.class }, message = "字典显示名称不能为空")
    @Excels({
            @Excel(name = "字典名称", sort = 1, groups = 1),
            @Excel(name = "枚举名称", sort = 1, groups = 2)
    })
    private String name;

    @ApiModelProperty(value = "描述")
    @Excels({
            @Excel(name = "字典描述", sort = 3, groups = 1),
            @Excel(name = "枚举描述", sort = 3, groups = 2)
    })
    private String remarks;

    @ApiModelProperty(value = "编码类型（01：编码种类；02：种类枚举）")
    private String dictType;

    @ApiModelProperty(value = "排序字段")
    private Integer sortNo;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
