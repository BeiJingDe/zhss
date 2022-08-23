package com.ylxx.cloud.system.config.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ylxx.cloud.core.annotation.ValidateModel;

/**
 * 系统参数
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ConfigVO对象", description="系统参数")
@ValidateModel
public class ConfigVO extends ConfigDO {

    private static final long serialVersionUID = 1L;


}
