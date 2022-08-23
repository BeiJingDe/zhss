package com.ylxx.cloud.upms.rel.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户和角色关联表
 *
 * @author caixiaopeng
 * @since 2020-09-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RelUserRoleDO对象", description="用户和角色关联表")
@TableName("t_base_rel_user_role")
public class RelUserRoleDO implements Serializable {
	
	// 说明：此model字段属性与数据库字段一一对应，不允许在此处添加数据没有的字段
	
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "角色编码")
    private String roleId;

}
