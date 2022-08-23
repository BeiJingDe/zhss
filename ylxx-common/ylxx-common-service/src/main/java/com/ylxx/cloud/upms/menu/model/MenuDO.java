package com.ylxx.cloud.upms.menu.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ylxx.cloud.core.annotation.ValidateModel;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 菜单（按钮）信息表
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MenuDO对象", description="菜单（按钮）信息表")
@TableName("t_base_menu")
public class MenuDO implements Serializable {
	
	// 说明：此model字段属性与数据库字段一一对应，不允许在此处添加数据没有的字段
	
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "主键")
    @NotBlank(groups = {ValidateGroup.Update.class}, message = "主键不能为空")
    private String id;

    @ApiModelProperty(value = "父级主键（一级菜单默认为：/）")
    private String parentId;

    @ApiModelProperty(value = "类型（01：菜单；02：按钮）")
    private String menuType;

    @ApiModelProperty(value = "菜单（按钮）名称")
    @NotBlank(groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class}, message = "菜单名称不能为空")
    private String menuName;

    @ApiModelProperty(value = "菜单（按钮）编码")
    @NotBlank(groups = {ValidateGroup.Insert.class, ValidateGroup.Update.class}, message = "菜单编码不能为空")
    private String menuCode;

    @ApiModelProperty(value = "组件名称")
    private String componentName;

    @ApiModelProperty(value = "组件路径")
    private String componentPath;

    @ApiModelProperty(value = "菜单请求url")
    private String requestUrl;

    @ApiModelProperty(value = "菜单请求重定向url")
    private String redirectUrl;

    @ApiModelProperty(value = "菜单图标")
    private String menuIcon;

    @ApiModelProperty(value = "排序字段")
    private Integer sortNo;

    @ApiModelProperty(value = "是否聚合子路由（0：否；1：是）")
    private String isAlwaysShow;

    @ApiModelProperty(value = "是否路由菜单（0：否；1：是）")
    private String isRoute;

    @ApiModelProperty(value = "是否叶子节点（0：否；1：是）")
    private String isLeaf;

    @ApiModelProperty(value = "是否缓存该页面（0：否；1：是）")
    private String isKeepAlive;

    @ApiModelProperty(value = "是否隐藏路由（0：否；1：是）")
    private String isHidden;

    @ApiModelProperty(value = "创建用户")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新用户")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
