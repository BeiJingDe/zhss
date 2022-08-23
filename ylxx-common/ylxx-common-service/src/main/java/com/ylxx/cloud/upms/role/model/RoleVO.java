package com.ylxx.cloud.upms.role.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 角色信息表
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="RoleVO对象", description="角色信息表")
@ValidateModel
public class RoleVO extends RoleDO {

	// 说明：此model继承了RoleDO，为数据库分页查询selectPageVo返回对象，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单编码数组")
    private List<String> menuCodes;


}
