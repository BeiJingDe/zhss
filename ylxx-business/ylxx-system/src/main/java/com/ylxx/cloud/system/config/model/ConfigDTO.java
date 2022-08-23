package com.ylxx.cloud.system.config.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ylxx.cloud.core.annotation.ValidateModel;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 系统参数
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ConfigDTO对象", description="系统参数")
@ValidateModel
public class ConfigDTO extends ConfigDO {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "查询的页码，默认第1页")
    private long current = 1;

    @ApiModelProperty(value = "查询每页条数，默认10条")
    private long size = 10;
        
    @ApiModelProperty(value = "排序字段（JSON数组字符串），有先后顺序，例：[{column:'create_time',asc:true},{column:'update_time',asc:false}]，说明：按照字段create_time升序，update_time降序排列")
    private String orders;
    
    @ApiModelProperty(value = "模糊查询字段")
    private String searchName;

    @ApiModelProperty(value = "模糊查询输入配置分组")
    private String configGroupQuery;

    @ApiModelProperty(value = "模糊查询输入配置名称")
    private String keyQuery;

}
