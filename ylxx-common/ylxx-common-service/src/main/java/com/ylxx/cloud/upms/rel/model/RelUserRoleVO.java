package com.ylxx.cloud.upms.rel.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ylxx.cloud.core.annotation.ValidateModel;

/**
 * 用户和角色关联表
 *
 * @author caixiaopeng
 * @since 2020-09-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="RelUserRoleVO对象", description="用户和角色关联表")
@ValidateModel
public class RelUserRoleVO extends RelUserRoleDO {

	// 说明：此model继承了RelUserRoleDO，为数据库分页查询selectPageVo返回对象，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;


}
