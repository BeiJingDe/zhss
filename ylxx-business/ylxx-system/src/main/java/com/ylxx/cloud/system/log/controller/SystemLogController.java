package com.ylxx.cloud.system.log.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.weiguangfu.swagger2.plus.annotation.ApiPlus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
//import com.ylxx.cloud.core.util.Assert;
import com.ylxx.cloud.core.validategroup.ValidateGroup;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
//import com.ylxx.cloud.system.log.enums.LogLevelEnums;
//import com.ylxx.cloud.system.log.enums.LogTypeEnums;
import com.ylxx.cloud.system.log.enums.LogLevelEnums;
import com.ylxx.cloud.system.log.enums.LogTypeEnums;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.log.model.SystemLogDTO;
import com.ylxx.cloud.system.log.model.SystemLogVO;
import com.ylxx.cloud.system.log.service.ISystemLogService;
import com.ylxx.cloud.system.validate.util.ValidateRequiredUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 系统日志 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-07-03
 */
@RestController
@RequestMapping(ApiVersionConsts.V1 + "/system/log")
@Api(tags = "系统管理：系统日志控制层")
@LogAnnotation(module = "系统日志", logType = LogTypeEnums.SYSTEM)
public class SystemLogController extends BaseController {

	@Resource
	private ISystemLogService systemLogService;
	
	@PostMapping
	//@LogAnnotation(value = "分页查询", operateType = OperateTypeEnum.QUERY, logLevel = LogLevelEnums.HIGH)
	@ApiOperation(value = "分页查询系统日志")
	public ApiResult selectPageVo(@RequestBody @Validated SystemLogDTO systemLogDto) {
		if(StrUtil.isBlank(systemLogDto.getOrders())) {
			systemLogDto.setOrders("[{column:'T1.update_time',asc:false}]");
		}
		Page<SystemLogVO> result = systemLogService.selectPageVo(systemLogDto);
		return ApiResultBuilder.success(result);
	}

	@PostMapping("/operateTypeEnum")
	//@LogAnnotation(value = "获取系统日志操作类型枚举", operateType = OperateTypeEnum.QUERY, logLevel = LogLevelEnums.HIGH)
	@ApiOperation(value = "获取系统日志操作类型枚举")
	public ApiResult getOperateTypeEnum() {
		Map<String, Object> result = CollUtil.newHashMap();
		for (OperateTypeEnum o : OperateTypeEnum.values()) {
			result.put(o.getValue(), o.getDescription());
		}
		return ApiResultBuilder.success(result);
	}

	@PostMapping("/backup")
	@LogAnnotation(value = "日志备份", operateType = OperateTypeEnum.BACKUP, logLevel = LogLevelEnums.HIGH)
	@ApiOperation(value = "日志备份")
	public ApiResult backup() {
		systemLogService.backup();
		return ApiResultBuilder.success();
	}

	@PostMapping("/recovery")
	@LogAnnotation(value = "日志恢复", operateType = OperateTypeEnum.RECOVERY, logLevel = LogLevelEnums.HIGH)
	@ApiOperation(value = "日志恢复")
	public ApiResult recovery() {
		systemLogService.recovery();
		return ApiResultBuilder.success();
	}

}

