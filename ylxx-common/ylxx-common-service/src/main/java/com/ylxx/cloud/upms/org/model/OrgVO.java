package com.ylxx.cloud.upms.org.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织信息表
 *
 * @author caixiaopeng
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="OrgVO对象", description="组织信息表")
@ValidateModel
public class OrgVO extends OrgDO {

	// 说明：此model继承了OrgDO，为数据库分页查询selectPageVo返回对象，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;


}
