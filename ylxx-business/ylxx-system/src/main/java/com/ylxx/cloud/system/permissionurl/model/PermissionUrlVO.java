package com.ylxx.cloud.system.permissionurl.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ylxx.cloud.core.annotation.ValidateModel;

/**
 * 允许的url名单
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="PermissionUrlVO对象", description="允许的url名单")
@ValidateModel
public class PermissionUrlVO extends PermissionUrlDO {

    private static final long serialVersionUID = 1L;


}
