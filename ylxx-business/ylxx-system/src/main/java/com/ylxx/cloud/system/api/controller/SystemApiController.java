package com.ylxx.cloud.system.api.controller;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.core.poi.ExcelUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.system.log.enums.LogLevelEnums;
import com.ylxx.cloud.system.log.enums.LogTypeEnums;
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
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import com.ylxx.cloud.system.api.model.SystemApiDO;
import com.ylxx.cloud.system.api.model.SystemApiDTO;
import com.ylxx.cloud.system.api.model.SystemApiVO;
import com.ylxx.cloud.system.api.service.ISystemApiService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 系统API接口管理表 前端控制器
 *
 * @author caixiaopeng
 * @since 2021-11-15
 */
@RestController
@Api(tags = "系统管理：系统API接口管理表控制层")
@LogAnnotation(module = "系统API接口管理表", logType = LogTypeEnums.SYSTEM)
@RequestMapping(ApiVersionConsts.V1 + "/system/api")
public class SystemApiController extends BaseController {

	@Resource
	private ISystemApiService systemApiService;
	
	@PostMapping("/select")
	@LogAnnotation(value = "分页查询", operateType = OperateTypeEnum.QUERY, logLevel = LogLevelEnums.HIGH)
	@ApiOperation(value = "分页查询")
	public ApiResult<Page<SystemApiVO>> selectPageVo(@RequestBody @Validated SystemApiDTO systemApiDto) {
		Page<SystemApiVO> result = systemApiService.selectPageVo(systemApiDto);
		return ApiResultBuilder.success(result);
	}

	@PostMapping("/syncApi")
	@LogAnnotation(value = "接口信息同步", operateType = OperateTypeEnum.SYNC, logLevel = LogLevelEnums.HIGH)
	@ApiOperation(value = "接口信息同步")
	public ApiResult syncApi() {
		systemApiService.syncApi();
		return ApiResultBuilder.success();
	}

}

