package com.ylxx.cloud.upms.role.controller;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.upms.role.consts.RoleConsts;
import com.ylxx.cloud.upms.role.util.RoleCacheUtil;
import com.ylxx.cloud.upms.user.model.UserVO;
import com.ylxx.cloud.upms.user.util.UserCacheUtil;
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
import com.ylxx.cloud.upms.role.model.RoleDO;
import com.ylxx.cloud.upms.role.model.RoleDTO;
import com.ylxx.cloud.upms.role.model.RoleVO;
import com.ylxx.cloud.upms.role.service.IRoleService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 * 角色信息表 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@RestController
@Api(tags = "系统管理：角色信息表控制层")
@RequestMapping(ApiVersionConsts.V1 + "/upms/role")
public class RoleController extends BaseController {

	@Resource
	private IRoleService roleService;
	
	@GetMapping
	@LogAnnotation(value = "分页查询", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "分页查询")
	public ApiResult selectPageVo(@Validated RoleDTO roleDto) {
		// 用户权限限制，不能查看权限等级比自己高的角色
		if(StrUtil.equals(ConfigConsts.VALUE_YES_1, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO())) {
			int roleSortNo = RoleCacheUtil.getMinSortNo(HttpServletUtil.getUsername()); // 当前登录用户的角色等级
			if(roleSortNo > RoleConsts.HIGHEST_LEVEL) {
				roleDto.setMinSortNo(roleSortNo);
			}
		}

		if(StrUtil.isBlank(roleDto.getOrders())) {
			roleDto.setOrders("[{column:'T1.update_time',asc:false}]");
		}
		Page<RoleVO> result = roleService.selectPageVo(roleDto);
		return ApiResultBuilder.success(result);
	}

	@PostMapping("/insert")
	@LogAnnotation(value = "新增记录", operateType = OperateTypeEnum.INSERT)
	@ApiOperation(value = "新增记录")
	public ApiResult insert(@RequestBody @Validated RoleVO roleVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Insert.class, roleVo);

		// 角色等级sortNo不能小于1
		if(roleVo.getSortNo() < RoleConsts.HIGHEST_LEVEL) {
			roleVo.setSortNo(RoleConsts.HIGHEST_LEVEL);
		}

		if(StrUtil.equals(ConfigConsts.VALUE_YES_1, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO())) {
			int roleSortNo = RoleCacheUtil.getMinSortNo(HttpServletUtil.getUsername()); // 当前登录用户的角色等级
			if(roleSortNo > RoleConsts.HIGHEST_LEVEL) { // 不是最高等级管理员
				if(roleVo.getSortNo() <= roleSortNo) {
					throw new ActionException(StrUtil.format("只能新增角色等级比自己低的角色，当前角色等级[{}]", roleSortNo));
				}
			}
		}

		roleService.insert(roleVo);
		return ApiResultBuilder.success(roleVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/update")
	@LogAnnotation(value = "修改记录", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "修改记录")
	public ApiResult update(@RequestBody @Validated RoleVO roleVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Update.class, roleVo);

		// 角色等级sortNo不能小于1
		if(roleVo.getSortNo() < RoleConsts.HIGHEST_LEVEL) {
			roleVo.setSortNo(RoleConsts.HIGHEST_LEVEL);
		}

		RoleVO old = roleService.selectById(roleVo.getId());
		if(ObjectUtil.isNull(old)) {
			throw new ActionException("修改的角色不存在");
		}

		if(StrUtil.equals(ConfigConsts.VALUE_YES_1, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO())) {
			int roleSortNo = RoleCacheUtil.getMinSortNo(HttpServletUtil.getUsername()); // 当前登录用户的角色等级
			if(roleSortNo > RoleConsts.HIGHEST_LEVEL) { // 不是最高等级管理员
				if(old.getSortNo() <= roleSortNo) {
					throw new ActionException(StrUtil.format("只能修改角色等级比自己低的角色，当前角色等级[{}]", roleSortNo));
				}
				if(roleVo.getSortNo() <= roleSortNo) {
					throw new ActionException(StrUtil.format("角色等级只能修改成比自己低，当前角色等级[{}]", roleSortNo));
				}
			}
		}

		roleService.update(roleVo);
		return ApiResultBuilder.success(roleVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/delete")
	@LogAnnotation(value = "批量删除", operateType = OperateTypeEnum.DELETE)
	@ApiOperation(value = "批量删除")
	public ApiResult delete(@RequestBody @Validated DeleteDTO deleteDto) {
		// 只能删除角色等级比自己低的角色
		if(ObjectUtil.isNotEmpty(deleteDto.getIds())) {
			if(StrUtil.equals(ConfigConsts.VALUE_YES_1, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO())) {
				int roleSortNo = RoleCacheUtil.getMinSortNo(HttpServletUtil.getUsername()); // 当前登录用户的角色等级
				if(roleSortNo > RoleConsts.HIGHEST_LEVEL) { // 不是最高等级管理员
					List<RoleDO> roleDos = roleService.selectByIds(deleteDto.getIds());
					if(ObjectUtil.isNotEmpty(roleDos)) {
						roleDos.forEach(item -> {
							if(item.getSortNo() <= roleSortNo) {
								throw new ActionException(StrUtil.format("只能删除角色等级比自己低的角色，当前角色等级[{}]", roleSortNo));
							}
						});
					}
				}
			}
		}

		roleService.deleteBatchIds(deleteDto.getIds());
		return ApiResultBuilder.success(deleteDto.getIds()).setMessage("操作成功");
	}

	@PostMapping("/changeRoleMenu")
	@LogAnnotation(value = "修改角色菜单", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "修改角色菜单")
	public ApiResult changeRoleMenu(@RequestBody @Validated RoleVO roleVo) {
		if(StrUtil.isBlank(roleVo.getRoleCode())) {
			throw new ActionException("角色编码不能为空");
		}

		RoleVO old = roleService.selectRole(roleVo.getRoleCode());
		if(ObjectUtil.isNull(old)) {
			throw new ActionException("修改的角色不存在");
		}

		if(StrUtil.equals(ConfigConsts.VALUE_YES_1, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO())) {
			int roleSortNo = RoleCacheUtil.getMinSortNo(HttpServletUtil.getUsername()); // 当前登录用户的角色等级
			if(roleSortNo > RoleConsts.HIGHEST_LEVEL) { // 不是最高等级管理员
				if(old.getSortNo() <= roleSortNo) {
					throw new ActionException(StrUtil.format("只能修改角色等级比自己低的角色，当前角色等级[{}]", roleSortNo));
				}
			}
		}

		roleService.changeRoleMenu(roleVo.getRoleCode(), roleVo.getMenuCodes());
		return ApiResultBuilder.success().setMessage("操作成功");
	}
	
}

