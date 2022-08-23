package com.ylxx.cloud.upms.menu.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class MenuVOTmp {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "父级主键（一级菜单默认为：/）")
    private String parentId;

    @ApiModelProperty(value = "类型（01：菜单；02：按钮）")
    private String menuType;

    @ApiModelProperty(value = "菜单（按钮）名称 menu_name")
    private String title;

    private String icon;

    @ApiModelProperty(value = "菜单（按钮）编码 menuCode")
    private String name;

    private Boolean hidden;

    private Boolean isLeaf;

    private String component;

    private String path;

    private Meta meta;

    private List<MenuVOTmp> children;
}
