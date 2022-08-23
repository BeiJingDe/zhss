package com.ylxx.cloud.system.config.controller;

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
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.model.ConfigDTO;
import com.ylxx.cloud.system.config.model.ConfigVO;
import com.ylxx.cloud.system.config.service.IConfigService;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.validate.util.ValidateRequiredUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统参数 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@RestController
@Api(tags = "系统管理：系统参数控制层")
@RequestMapping(ApiVersionConsts.V1 + "/system/config")
@LogAnnotation(module = "系统参数")
public class ConfigController extends BaseController {

	@Resource
	private IConfigService configService;
	
	@GetMapping
	@LogAnnotation(value = "分页查询系统参数", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "分页查询系统参数")
	public ApiResult selectPageVo(@Validated ConfigDTO configDto) {
		if(StrUtil.isBlank(configDto.getOrders())) {
			configDto.setOrders("[{column:'T1.update_time',asc:false}]");
		}
		Page<ConfigVO> result = configService.selectPageVo(configDto);
		return ApiResultBuilder.success(result);
	}

	@PostMapping("/insert")
	@LogAnnotation(value = "新增系统参数", operateType = OperateTypeEnum.INSERT)
	@ApiOperation(value = "新增系统参数")
	public ApiResult insert(@RequestBody @Validated ConfigVO configVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Insert.class, configVo);

		configService.insert(configVo);
		return ApiResultBuilder.success(configVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/update")
	@LogAnnotation(value = "修改系统参数", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "修改系统参数")
	public ApiResult update(@RequestBody @Validated ConfigVO configVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Update.class, configVo);

		configService.update(configVo);
		return ApiResultBuilder.success(configVo.getId()).setMessage("操作成功");
	}
	
//	@PostMapping("/delete")
//	@LogAnnotation(value = "批量删除系统参数", operateType = OperateTypeEnum.DELETE)
//	@ApiOperation(value = "批量删除系统参数")
	public ApiResult delete(@RequestBody @Validated DeleteDTO deleteDto) {
		configService.deleteBatchIds(deleteDto.getIds());
		return ApiResultBuilder.success(deleteDto.getIds()).setMessage("操作成功");
	}
	
	@GetMapping("/refresh-cache")
	@LogAnnotation(value = "刷新系统参数缓存", operateType = OperateTypeEnum.REFRESH)
	@ApiOperation(value = "刷新系统参数缓存")
	public ApiResult refreshCache() {
		configService.refreshCache();
		return ApiResultBuilder.success().setMessage("缓存刷新成功");
	}

	@GetMapping("/rsa-public-key")
	@LogAnnotation(value = "获取RSA公钥", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "获取RSA公钥")
	public ApiResult getRsaPublicKey() {
		String rsaPublicKey = ConfigCacheValue.SYSTEM_KEY_RSA_PUBLIC_KEY();
		return ApiResultBuilder.success(rsaPublicKey);
	}

}

