package com.ylxx.cloud.upms.rel.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户和组织关联表
 *
 * @author caixiaopeng
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RelUserOrgDO对象", description="用户和组织关联表")
@TableName("t_base_rel_user_org")
public class RelUserOrgDO implements Serializable {
	
	// 说明：此model字段属性与数据库字段一一对应，不允许在此处添加数据没有的字段
	
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "组织ID")
    private String orgId;

}
