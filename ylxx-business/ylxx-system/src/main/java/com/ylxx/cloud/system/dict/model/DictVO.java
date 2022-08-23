package com.ylxx.cloud.system.dict.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ylxx.cloud.core.annotation.ValidateModel;

import java.util.List;

/**
 * 数据字典
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="DictVO对象", description="数据字典")
@ValidateModel
public class DictVO extends DictDO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "子类型数量")
    private Integer childNum;

}
