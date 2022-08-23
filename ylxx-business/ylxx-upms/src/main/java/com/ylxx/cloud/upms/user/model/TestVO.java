package com.ylxx.cloud.upms.user.model;

import com.ylxx.cloud.core.annotation.ValidateModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Data
@ApiModel(value="测试", description="测试")
@ValidateModel
public class TestVO  {

	// 说明：此model继承了UserDO，为数据库分页查询selectPageVo返回对象，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;

    private String test1;

    private String test2;
    private Map<String, String> test3;
    private String[] test4;

}
