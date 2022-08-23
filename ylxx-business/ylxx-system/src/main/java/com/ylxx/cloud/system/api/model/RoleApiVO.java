package com.ylxx.cloud.system.api.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ylxx.cloud.core.annotation.ValidateModel;

/**
 * 角色API接口权限表
 *
 * @author caixiaopeng
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="RoleApiVO对象", description="角色API接口权限表")
@ValidateModel
public class RoleApiVO extends RoleApiDO {

	// 说明：此model继承了RoleApiDO，为数据库分页查询selectPageVo返回对象，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;


}
