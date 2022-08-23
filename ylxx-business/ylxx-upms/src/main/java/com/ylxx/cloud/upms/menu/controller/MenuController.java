package com.ylxx.cloud.upms.menu.controller;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.upms.role.consts.RoleConsts;
import com.ylxx.cloud.upms.role.util.RoleCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.validate.util.ValidateRequiredUtil;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import com.ylxx.cloud.upms.menu.model.MenuDO;
import com.ylxx.cloud.upms.menu.model.MenuDTO;
import com.ylxx.cloud.upms.menu.model.MenuVO;
import com.ylxx.cloud.upms.menu.service.IMenuService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 * 菜单（按钮）信息表 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@RestController
@Api(tags = "系统管理：菜单（按钮）信息表控制层")
@RequestMapping(ApiVersionConsts.V1 + "/upms/menu")
public class MenuController extends BaseController {

	@Resource
	private IMenuService menuService;
	
	@GetMapping
	@LogAnnotation(value = "分页查询", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "分页查询")
	public ApiResult selectPageVo(@Validated MenuDTO menuDto) {
		// 用户权限限制，只能查看自己拥有权限的菜单
		if(StrUtil.equals(ConfigConsts.VALUE_YES_1, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO())) {
			int roleSortNo = RoleCacheUtil.getMinSortNo(HttpServletUtil.getUsername()); // 当前登录用户的角色等级
			if(roleSortNo > RoleConsts.HIGHEST_LEVEL) {
				menuDto.setUsername(HttpServletUtil.getUsername());
			}
		}

		Page<MenuVO> result = menuService.selectPageVo(menuDto);
		return ApiResultBuilder.success(result);
	}

	@PostMapping("/insert")
	@LogAnnotation(value = "新增记录", operateType = OperateTypeEnum.INSERT)
	@ApiOperation(value = "新增记录")
	public ApiResult insert(@RequestBody @Validated MenuVO menuVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Insert.class, menuVo);

		menuService.insert(menuVo);
		return ApiResultBuilder.success(menuVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/update")
	@LogAnnotation(value = "修改记录", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "修改记录")
	public ApiResult update(@RequestBody @Validated MenuVO menuVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Update.class, menuVo);

		menuService.update(menuVo);
		return ApiResultBuilder.success(menuVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/delete")
	@LogAnnotation(value = "批量删除", operateType = OperateTypeEnum.DELETE)
	@ApiOperation(value = "批量删除")
	public ApiResult delete(@RequestBody @Validated DeleteDTO deleteDto) {
		menuService.deleteBatchIds(deleteDto.getIds());
		return ApiResultBuilder.success(deleteDto.getIds()).setMessage("操作成功");
	}
	
}

