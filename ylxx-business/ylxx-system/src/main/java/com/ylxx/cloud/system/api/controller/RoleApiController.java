package com.ylxx.cloud.system.api.controller;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import com.ylxx.cloud.core.model.DeleteDTO;
import com.ylxx.cloud.core.poi.ExcelUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.Assert;
import com.ylxx.cloud.system.log.enums.LogLevelEnums;
import com.ylxx.cloud.system.log.enums.LogTypeEnums;
import com.ylxx.cloud.util.StringUtils;
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
import com.ylxx.cloud.system.api.model.RoleApiDO;
import com.ylxx.cloud.system.api.model.RoleApiDTO;
import com.ylxx.cloud.system.api.model.RoleApiVO;
import com.ylxx.cloud.system.api.service.IRoleApiService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * ??????API??????????????? ???????????????
 *
 * @author caixiaopeng
 * @since 2021-11-15
 */
@RestController
@Api(tags = "?????????????????????API????????????????????????")
@LogAnnotation(module = "??????API???????????????", logType = LogTypeEnums.SYSTEM)
@RequestMapping(ApiVersionConsts.V1 + "/system/roleapi")
public class RoleApiController extends BaseController {

	@Resource
	private IRoleApiService roleApiService;
	
	@PostMapping("/select")
	@LogAnnotation(value = "????????????", operateType = OperateTypeEnum.QUERY, logLevel = LogLevelEnums.HIGH)
	@ApiOperation(value = "????????????")
	public ApiResult<Page<RoleApiVO>> selectPageVo(@RequestBody @Validated RoleApiDTO roleApiDto) {
		Page<RoleApiVO> result = roleApiService.selectPageVo(roleApiDto);
		return ApiResultBuilder.success(result);
	}
	
	@PostMapping("/update")
	@LogAnnotation(value = "????????????", operateType = OperateTypeEnum.UPDATE, logLevel = LogLevelEnums.HIGH)
	@ApiOperation(value = "????????????")
	public ApiResult update(@RequestBody @Validated RoleApiVO roleApiVo) {
		Assert.notBlank(roleApiVo.getId(), "??????????????????");

		//roleApiService.update(roleApiVo);
		return ApiResultBuilder.success(roleApiVo.getId()).setMessage("????????????");
	}

	@PostMapping("/refreshCache")
	@LogAnnotation(value = "????????????", operateType = OperateTypeEnum.REFRESH, logLevel = LogLevelEnums.HIGH)
	@ApiOperation(value = "????????????")
	public ApiResult refreshCache() {
		roleApiService.refreshCache();
		return ApiResultBuilder.success();
	}

}

