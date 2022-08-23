package com.ylxx.cloud.system.validate.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.weiguangfu.swagger2.plus.annotation.ApiPlus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.validate.model.ValidateDTO;
import com.ylxx.cloud.system.validate.model.ValidateVO;
import com.ylxx.cloud.system.validate.service.IValidateService;
import com.ylxx.cloud.system.validate.util.ValidateModelCacheUtil;
import com.ylxx.cloud.system.validate.util.ValidateRequiredUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 前后端一致校验规则表 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-07-05
 */
@RestController
@Api(tags = "系统管理：前后端一致校验规则表控制层")
@RequestMapping(ApiVersionConsts.V1 + "/system/validate")
@LogAnnotation(module = "校验规则")
public class ValidateController extends BaseController {

	@Resource
	private IValidateService validateService;
	
	@GetMapping
	@LogAnnotation(value = "分页查询校验规则", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "分页查询校验规则")
	public ApiResult selectPageVo(@Validated ValidateDTO validateDto) {
		if(StrUtil.isBlank(validateDto.getOrders())) {
			validateDto.setOrders("[{column:'T1.update_time',asc:false}]");
		}
		Page<ValidateVO> result = validateService.selectPageVo(validateDto);
		return ApiResultBuilder.success(result);
	}

	@PostMapping("/insert")
	@LogAnnotation(value = "新增校验规则", operateType = OperateTypeEnum.INSERT)
	@ApiOperation(value = "新增校验规则")
	public ApiResult insert(@RequestBody @Validated ValidateVO validateVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Insert.class, validateVo);

		validateService.insert(validateVo);
		return ApiResultBuilder.success(validateVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/update")
	@LogAnnotation(value = "修改校验规则", operateType = OperateTypeEnum.UPDATE)
	@ApiOperation(value = "修改校验规则")
	public ApiResult update(@RequestBody @Validated ValidateVO validateVo) {
		// 校验必填参数
		ValidateRequiredUtil.validate(ValidateGroup.Update.class, validateVo);

		validateService.update(validateVo);
		return ApiResultBuilder.success(validateVo.getId()).setMessage("操作成功");
	}

	@PostMapping("/delete")
	@LogAnnotation(value = "批量删除校验规则", operateType = OperateTypeEnum.DELETE)
	@ApiOperation(value = "批量删除校验规则")
	public ApiResult delete(@RequestBody @Validated DeleteDTO deleteDto) {
		validateService.deleteBatchIds(deleteDto.getIds());
		return ApiResultBuilder.success(deleteDto.getIds()).setMessage("操作成功");
	}
	
	@GetMapping("/refresh-cache")
	@LogAnnotation(value = "刷新校验规则缓存", operateType = OperateTypeEnum.REFRESH)
	@ApiOperation(value = "刷新校验规则缓存")
	public ApiResult refreshCache() {
		// 刷新正则缓存
		validateService.refreshCache();
		validateService.refreshCacheModel();
		return ApiResultBuilder.success().setMessage("缓存刷新成功");
	}

//	@GetMapping("/refresh-cache-model")
//	@LogAnnotation(value = "刷新校验model缓存", operateType = OperateTypeEnum.REFRESH)
//	@ApiOperation(value = "刷新校验model缓存")
	public ApiResult refreshCacheModel() {
		// 刷新一致校验model缓存
		validateService.refreshCacheModel();
		return ApiResultBuilder.success().setMessage("缓存刷新成功");
	}

	@GetMapping("/cache-model")
	@LogAnnotation(value = "获取缓存中的一致校验model", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "获取缓存中的一致校验model")
	public ApiResult getCacheModel() {
		JSONObject result = new JSONObject();
		
		Set<String> keys = ValidateModelCacheUtil.getAllKeys();
		for(String key : keys) {
			String[] ss = key.split(SystemConsts.CONNECTOR);
			String modelRule = ValidateModelCacheUtil.getModelRule(ss[1], ss[2]);
//			result.put(StrUtil.format("{}#{}", ss[1], ss[2]), JSON.parse(modelRule));
			result.put(StrUtil.format("{}", ss[2]), JSON.parse(modelRule));
		}
		
		return ApiResultBuilder.success(result);
	}

	@GetMapping("/validateResult")
	@LogAnnotation(value = "获取正则校验结果", operateType = OperateTypeEnum.EXECUTE)
	@ApiOperation(value = "获取正则校验结果")
	public ApiResult getValidateResult(String regex, String value) {
		boolean result = Pattern.matches(regex, value);
		return ApiResultBuilder.success(StrUtil.format("正则匹配返回结果为：{}", result));
	}


	
}

