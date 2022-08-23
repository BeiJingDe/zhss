package com.ylxx.cloud.upms.rel.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户和组织关联表
 *
 * @author caixiaopeng
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="RelUserOrgVO对象", description="用户和组织关联表")
@ValidateModel
public class RelUserOrgVO extends RelUserOrgDO {

	// 说明：此model继承了RelUserOrgDO，为数据库分页查询selectPageVo返回对象，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;


}
