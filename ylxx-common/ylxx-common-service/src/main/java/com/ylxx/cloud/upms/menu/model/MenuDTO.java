package com.ylxx.cloud.upms.menu.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 菜单（按钮）信息表
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MenuDTO对象", description="菜单（按钮）信息表")
@ValidateModel
public class MenuDTO extends MenuDO {

	// 说明：此model继承了MenuDO，controller查询使用此model接收参数，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "查询的页码，默认第1页")
    private long current = 1;

    @ApiModelProperty(value = "查询每页条数，默认10条")
    private long size = 10;

    @ApiModelProperty(value = "排序字段（JSON数组字符串），有先后顺序，例：[{column:'create_time',asc:true},{column:'update_time',asc:false}]，说明：按照字段create_time升序，update_time降序排列")
    private String orders;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "角色编码数组")
    private List<String> roleCodes;

    @ApiModelProperty(value = "菜单编码数组")
    private List<String> menuCodes;

}
