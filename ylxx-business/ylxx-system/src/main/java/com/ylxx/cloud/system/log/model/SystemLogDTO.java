package com.ylxx.cloud.system.log.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 系统日志
 *
 * @author caixiaopeng
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SystemLogDTO对象", description="系统日志")
@ValidateModel
public class SystemLogDTO extends SystemLogDO {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "查询的页码，默认第1页")
    private long current = 1;

    @ApiModelProperty(value = "查询每页条数，默认10条")
    private long size = 10;
        
    @ApiModelProperty(value = "排序字段（JSON数组字符串），有先后顺序，例：[{column:'create_time',asc:true},{column:'update_time',asc:false}]，说明：按照字段create_time升序，update_time降序排列")
    private String orders;

    @ApiModelProperty("起始查询时间")
    private Date beginTime;

    @ApiModelProperty("截止查询时间")
    private Date endTime;

    private String menuName;

    private String createUser;

}
