package com.ylxx.cloud.core.controller;

import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.core.service.ICommonCoreService;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.runner.ValidateModelRunner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 
 * @ClassName: CommonController 
 * @Description: 每个微服务都需要的基础服务
 * @author: caixiaopeng
 * @date: 2020年6月7日 上午1:22:15
 */
@RestController
@RequestMapping(ApiVersionConsts.V1 + "/common")
@Api(tags = "公用基础服务控制层")
public class CommonCoreController extends BaseController {

	@Resource
	private ValidateModelRunner validateModelRunner;
	@Resource
	private ICommonCoreService commonCoreService;
	
	@GetMapping("/refresh-validate-model")
	@LogAnnotation(value = "刷新前后端一致校验model缓存", operateType = OperateTypeEnum.REFRESH)
	@ApiOperation(value = "刷新前后端一致校验model缓存", httpMethod = "GET")
	public ApiResult refreshValidateModel() {
		validateModelRunner.run(null);
		return ApiResultBuilder.success().setMessage("刷新成功");
	}

	@GetMapping("/now")
	@LogAnnotation(value = "获取服务器和数据库的当前时间", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "获取服务器和数据库的当前时间", httpMethod = "GET")
	public ApiResult now() {
		Map<String, Object> result = commonCoreService.now();
		return ApiResultBuilder.success(result);
	}

}
