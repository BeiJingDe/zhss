package com.ylxx.cloud.upms.user.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 用户信息表
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="UserVO对象", description="用户信息表")
@ValidateModel
public class UserVO extends UserDO {

	// 说明：此model继承了UserDO，为数据库分页查询selectPageVo返回对象，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色编码数组")
    private List<String> roleCodes;
    
    @ApiModelProperty("组织编码数组")
    private List<String> orgCodes;

    @ApiModelProperty(value = "用户最高角色等级，数值越小，权限越大")
    private Integer roleSortNo;

}
