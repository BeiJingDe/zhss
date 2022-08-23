package com.ylxx.cloud.upms.role.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色信息表
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="RoleDTO对象", description="角色信息表")
@ValidateModel
public class RoleDTO extends RoleDO {

	// 说明：此model继承了RoleDO，controller查询使用此model接收参数，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "查询的页码，默认第1页")
    private long current = 1;

    @ApiModelProperty(value = "查询每页条数，默认10条")
    private long size = 10;

    @ApiModelProperty(value = "排序字段（JSON数组字符串），有先后顺序，例：[{column:'create_time',asc:true},{column:'update_time',asc:false}]，说明：按照字段create_time升序，update_time降序排列")
    private String orders;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty("角色编码数组")
    private List<String> roleCodes;

    @ApiModelProperty(value = "模糊查询输入角色编码条件")
    private String roleCodeQuery;

    @ApiModelProperty(value = "最小排序字段")
    private Integer minSortNo;

}
