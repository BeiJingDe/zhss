package com.ylxx.cloud.system.log.model;

import com.ylxx.cloud.core.annotation.ValidateModel;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统日志
 *
 * @author caixiaopeng
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SystemLogVO对象", description="系统日志")
@ValidateModel
public class SystemLogVO extends SystemLogDO {

    private static final long serialVersionUID = 1L;

    private String menuName;

    private String createUser;

}
