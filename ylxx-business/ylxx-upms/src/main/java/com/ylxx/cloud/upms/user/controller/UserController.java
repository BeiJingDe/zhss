package com.ylxx.cloud.upms.user.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.core.poi.ExcelUtil;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.validate.util.ValidateRequiredUtil;
import com.ylxx.cloud.upms.role.consts.RoleConsts;
import com.ylxx.cloud.upms.role.util.RoleCacheUtil;
import com.ylxx.cloud.upms.user.consts.UserConsts;
import com.ylxx.cloud.upms.user.model.UserDTO;
import com.ylxx.cloud.upms.user.model.UserVO;
import com.ylxx.cloud.upms.user.service.IUserService;
import com.ylxx.cloud.upms.user.util.UserCacheUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户信息表 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-08-26
 */
@RestController
@Api(tags = "系统管理：用户信息表控制层")
@RequestMapping(ApiVersionConsts.V1 + "/upms/user")
public class UserController extends BaseController {

	@Resource
	private IUserService userService;
	
	@GetMapping
	@LogAnnotation(value = "分页查询", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "分页查询")
	public ApiResult selectPageVo(@Validated UserDTO userDto) {
		if(StrUtil.isBlank(userDto.getOrders())) {
			userDto.setOrders("[{column:'T1.update_time',asc:false}]");
		}
		Page<UserVO> result = userService.selectPageVo(userDto);
		return ApiResultBuilder.success(result);
	}

    // 临时处理
    @PostMapping("/list")
    @LogAnnotation(value = "分页查询", operateType = OperateTypeEnum.QUERY)
    @ApiOperation(value = "分页查询")
    public ApiResult selectPageVoList(@Validated UserDTO userDto) {
        if (StrUtil.isBlank(userDto.getOrders())) {
            userDto.setOrders("[{column:'T1.update_time',asc:false}]");
        }
        Page<String> result = userService.selectPageVoList(userDto);
        return ApiResultBuilder.success(result);
    }

    @PostMapping("/insert")
    @LogAnnotation(value = "新增记录", operateType = OperateTypeEnum.INSERT)
    @ApiOperation(value = "新增记录")
    public ApiResult insert(@RequestBody @Validated UserVO userVo) {
        // 校验必填参数
        ValidateRequiredUtil.validate(ValidateGroup.Insert.class, userVo);

		userService.insert(userVo);
		userService.changeUserRole(userVo.getUsername(), userVo.getRoleCodes());
		userService.changeUserOrg(userVo.getUsername(), userVo.getOrgCodes());
		return ApiResultBuilder.success(userVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/update")
	@LogAnnotation(value = "修改记录", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "修改记录")
	public ApiResult update(@RequestBody @Validated UserVO userVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Update.class, userVo);

		UserVO old = userService.selectById(userVo.getId());
		if(ObjectUtil.isNull(old)) {
			throw new ActionException("修改的用户不存在");
		}

		// 用户权限限制
		if(StrUtil.equals(ConfigConsts.VALUE_YES_1, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO())) {
			String username = HttpServletUtil.getUsername();
			int roleSortNo = RoleCacheUtil.getMinSortNo(username); // 当前登录用户的角色等级
			if(!StrUtil.equals(username, old.getUsername()) && roleSortNo > RoleConsts.HIGHEST_LEVEL && ObjectUtil.isNotNull(old.getRoleSortNo()) && old.getRoleSortNo() <= roleSortNo) {
				throw new ActionException("只能操作角色等级比自己低的用户"); // 管理员拥有所有权
			}
		}

		userService.update(userVo);
		userService.changeUserRole(old.getUsername(), userVo.getRoleCodes());
		userService.changeUserOrg(old.getUsername(), userVo.getOrgCodes());
		return ApiResultBuilder.success(userVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/delete")
	@LogAnnotation(value = "批量删除", operateType = OperateTypeEnum.DELETE)
	@ApiOperation(value = "批量删除")
	public ApiResult delete(@RequestBody @Validated DeleteDTO deleteDto) {

		// 用户权限限制
		if(StrUtil.equals(ConfigConsts.VALUE_YES_1, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO())) {
			List<UserVO> userVos = userService.selectByIds(deleteDto.getIds());
			if(ObjectUtil.isNotEmpty(userVos)) {
				int roleSortNo = RoleCacheUtil.getMinSortNo(HttpServletUtil.getUsername()); // 当前登录用户的角色等级
				userVos.forEach(item -> {
					if(roleSortNo > RoleConsts.HIGHEST_LEVEL && ObjectUtil.isNotNull(item.getRoleSortNo()) && item.getRoleSortNo() <= roleSortNo) {
						throw new ActionException("只能操作角色等级比自己低的用户"); // 管理员拥有所有权
					}
				});
			}
		}

		userService.deleteBatchIds(deleteDto.getIds());
		return ApiResultBuilder.success(deleteDto.getIds()).setMessage("操作成功");
	}

	@PostMapping("/updateMmpd")
	@LogAnnotation(value = "修改密码", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "修改密码")
	public ApiResult updateMmpd(@RequestBody @Validated UserVO userVo) {
		if(StrUtil.isBlank(userVo.getId())) {
			throw new ActionException("用户ID不能为空");
		}
		if(StrUtil.isBlank(userVo.getMmpd())) {
			throw new ActionException("用户密码不能为空");
		}

		UserVO old = userService.selectById(userVo.getId());
		if(ObjectUtil.isNull(old)) {
			throw new ActionException("修改的用户不存在");
		}

		// 用户权限限制
		if(StrUtil.equals(ConfigConsts.VALUE_YES_1, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO())) {
			String username = HttpServletUtil.getUsername();
			int roleSortNo = RoleCacheUtil.getMinSortNo(username); // 当前登录用户的角色等级
			if(!StrUtil.equals(username, old.getUsername()) && roleSortNo > RoleConsts.HIGHEST_LEVEL && ObjectUtil.isNotNull(old.getRoleSortNo()) && old.getRoleSortNo() <= roleSortNo) {
				throw new ActionException("只能操作角色等级比自己低的用户"); // 管理员拥有所有权
			}
		}

		userService.updateMmpd(userVo);
		return ApiResultBuilder.success().setMessage("操作成功");
	}

	@GetMapping("/userAndRealname")
	@LogAnnotation(value = "查询用户名和真实名字的映射关系", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "查询用户名和真实名字的映射关系")
	public ApiResult selectUserAndRealname() {
		List<Map<String, Object>> result = userService.selectUserAndRealname();
		return ApiResultBuilder.success(result);
	}

	@GetMapping("/export")
	@LogAnnotation(value = "用户数据导出", operateType = OperateTypeEnum.EXPORT)
	@ApiOperation(value = "用户数据导出")
	public void export(@Validated UserDTO userDto) {
		userDto.setCurrent(1);
		userDto.setSize(SystemConsts.MAX_PAGE_SIZE);
		ApiResult<Page<UserVO>> result = this.selectPageVo(userDto);
		List<UserVO> list = CollUtil.newArrayList();
		if(ObjectUtil.isNotNull(result.getData()) && ObjectUtil.isNotEmpty(result.getData().getRecords())) {
			list = result.getData().getRecords();
			list.forEach(item -> {
				item.setMmpd(null);
			});
		}
		ExcelUtil.export(UserVO.class, list, "用户数据导出", HttpServletUtil.getResponse());
	}

	@GetMapping("/template")
	@LogAnnotation(value = "用户模板导出", operateType = OperateTypeEnum.EXPORT)
	@ApiOperation(value = "用户模板导出")
	public void template() {
		UserVO userVo = new UserVO();
		userVo.setUsername("test");
		userVo.setRealname("测试用户");
		userVo.setMmpd("88888888");
		userVo.setGender(UserConsts.GENDER_TYPE_1);
		userVo.setBirthday(new Date());
		userVo.setPhone("18500001111");
		userVo.setEmail("123@qq.com");
		userVo.setRemarks("备注");
		List<UserVO> list = CollUtil.newArrayList(userVo);
		ExcelUtil.export(UserVO.class, list, "用户模板导出", HttpServletUtil.getResponse());
	}

	@PostMapping("/import")
	@LogAnnotation(value = "用户数据导入", operateType = OperateTypeEnum.IMPORT)
	@ApiOperation(value = "用户数据导入")
	public ApiResult importData(MultipartFile file) {
		int n = userService.importData(file);
		return ApiResultBuilder.success().setMessage(StrUtil.format("成功导入{}条用户数据", n));
	}

}

