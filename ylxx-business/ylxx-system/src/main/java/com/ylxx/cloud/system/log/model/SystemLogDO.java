package com.ylxx.cloud.system.log.model;

import com.baomidou.mybatisplus.annotation.TableName;
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
@ApiModel(value="SystemLog对象", description="系统日志")
@TableName("t_system_log")
public class SystemLogDO extends SystemLogCore {

    private static final long serialVersionUID = 1L;

}
