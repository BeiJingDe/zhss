package com.ylxx.cloud.system.validate.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ylxx.cloud.core.annotation.ValidateModel;

/**
 * 前后端一致校验规则表
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ValidateVO对象", description="前后端一致校验规则表")
@ValidateModel
public class ValidateVO extends ValidateDO {

    private static final long serialVersionUID = 1L;


}
