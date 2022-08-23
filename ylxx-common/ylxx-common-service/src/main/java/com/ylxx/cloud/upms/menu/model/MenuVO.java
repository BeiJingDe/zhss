package com.ylxx.cloud.upms.menu.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单（按钮）信息表
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MenuVO对象", description="菜单（按钮）信息表")
@ValidateModel
public class MenuVO extends MenuDO {

	// 说明：此model继承了MenuDO，为数据库分页查询selectPageVo返回对象，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;


}
