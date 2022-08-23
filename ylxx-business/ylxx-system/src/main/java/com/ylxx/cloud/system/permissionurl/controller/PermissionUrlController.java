package com.ylxx.cloud.system.permissionurl.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.permissionurl.model.PermissionUrlDTO;
import com.ylxx.cloud.system.permissionurl.model.PermissionUrlVO;
import com.ylxx.cloud.system.permissionurl.service.IPermissionUrlService;
import com.ylxx.cloud.system.validate.util.ValidateRequiredUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * HTTP请求白名单 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@RestController
@Api(tags = "系统管理：HTTP请求白名单控制层")
@RequestMapping(ApiVersionConsts.V1 + "/system/permission-url")
@LogAnnotation(module = "白名单")
public class PermissionUrlController extends BaseController {

	@Resource
	private IPermissionUrlService permissionUrlService;
	
	@GetMapping
	@LogAnnotation(value = "分页查询白名单", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "分页查询白名单")
	public ApiResult selectPageVo(@Validated PermissionUrlDTO permissionUrlDto) {
		if(StrUtil.isBlank(permissionUrlDto.getOrders())) {
			permissionUrlDto.setOrders("[{column:'T1.update_time',asc:false}]");
		}
		Page<PermissionUrlVO> result = permissionUrlService.selectPageVo(permissionUrlDto);
		return ApiResultBuilder.success(result);
	}

	@PostMapping("/insert")
	@LogAnnotation(value = "新增白名单", operateType = OperateTypeEnum.INSERT)
	@ApiOperation(value = "新增白名单")
	public ApiResult insert(@RequestBody @Validated PermissionUrlVO permissionUrlVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Insert.class, permissionUrlVo);

		permissionUrlService.insert(permissionUrlVo);
		return ApiResultBuilder.success(permissionUrlVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/update")
	@LogAnnotation(value = "修改白名单", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "修改白名单")
	public ApiResult update(@RequestBody @Validated PermissionUrlVO permissionUrlVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Update.class, permissionUrlVo);

		permissionUrlService.update(permissionUrlVo);
		return ApiResultBuilder.success(permissionUrlVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/delete")
	@LogAnnotation(value = "批量删除白名单", operateType = OperateTypeEnum.DELETE)
	@ApiOperation(value = "批量删除白名单")
	public ApiResult delete(@RequestBody @Validated DeleteDTO deleteDto) {
		permissionUrlService.deleteBatchIds(deleteDto.getIds());
		return ApiResultBuilder.success(deleteDto.getIds()).setMessage("操作成功");
	}
	
	@GetMapping("/refresh-cache")
	@LogAnnotation(value = "刷新白名单缓存", operateType = OperateTypeEnum.REFRESH)
	@ApiOperation(value = "刷新白名单缓存")
	public ApiResult refreshCache() {
		permissionUrlService.refreshCache();
		return ApiResultBuilder.success().setMessage("缓存刷新成功");
	}
	
}

