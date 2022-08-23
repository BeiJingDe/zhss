package com.ylxx.cloud.system.validate.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 前后端一致校验规则表
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ValidateDTO对象", description="前后端一致校验规则表")
@ValidateModel
public class ValidateDTO extends ValidateDO {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "查询的页码，默认第1页")
    private long current = 1;

    @ApiModelProperty(value = "查询每页条数，默认10条")
    private long size = 10;
        
    @ApiModelProperty(value = "排序字段（JSON数组字符串），有先后顺序，例：[{column:'create_time',asc:true},{column:'update_time',asc:false}]，说明：按照字段create_time升序，update_time降序排列")
    private String orders;
    
}
