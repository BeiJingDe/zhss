package com.ylxx.cloud.system.appversion.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ylxx.cloud.core.annotation.ValidateModel;

/**
 * 手机APP版本管理
 *
 * @author caixiaopeng
 * @since 2021-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AppVersionVO对象", description="手机APP版本管理")
@ValidateModel
public class AppVersionVO extends AppVersionDO {

	// 说明：此model继承了AppVersionDO，为数据库分页查询selectPageVo返回对象，如果父类没有对应的条件属性，可在此处添加

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件原始名称")
    private String fileName;

    @ApiModelProperty(value = "文件存储路径")
    private String filePath;

}
