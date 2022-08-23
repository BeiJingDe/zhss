package com.ylxx.cloud.upms.org.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 组织信息表
 *
 * @author caixiaopeng
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="OrgDTO对象", description="组织信息表")
@ValidateModel
public class OrgDTO extends OrgDO {

	// 说明：此model继承了OrgDO，controller查询使用此model接收参数，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "查询的页码，默认第1页")
    private long current = 1;

    @ApiModelProperty(value = "查询每页条数，默认10条")
    private long size = 10;
        
    @ApiModelProperty(value = "排序字段（JSON数组字符串），有先后顺序，例：[{column:'create_time',asc:true},{column:'update_time',asc:false}]，说明：按照字段create_time升序，update_time降序排列")
    private String orders;

    @ApiModelProperty("模糊查询字段")
    private String searchName;

    @ApiModelProperty("起始查询时间")
    private Date beginTime;

    @ApiModelProperty("截止查询时间")
    private Date endTime;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "组织编码数组")
    private List<String> orgCodes;

    @ApiModelProperty(value = "模糊查询输入组织编码条件")
    private String orgCodeQuery;

    @ApiModelProperty(value = "组织类型")
    private String  orgTypes;

}
