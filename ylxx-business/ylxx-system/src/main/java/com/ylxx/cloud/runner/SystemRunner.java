package com.ylxx.cloud.runner;

import com.ylxx.cloud.system.api.service.IRoleApiService;
import com.ylxx.cloud.system.config.service.IConfigService;
import com.ylxx.cloud.system.dict.service.IDictService;
import com.ylxx.cloud.system.permissionurl.service.IPermissionUrlService;
import com.ylxx.cloud.system.validate.service.IValidateService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SystemRunner implements ApplicationRunner {

	@Resource
	private IValidateService validateService;
	@Resource
	private IConfigService configService;
	@Resource
	private IDictService dictService;
	@Resource
	private IPermissionUrlService permissionUrlService;
	@Resource
	private IRoleApiService roleApiService;

	@Override
	public void run(ApplicationArguments args) {
		// 刷新校验正则缓存
		validateService.refreshCache();
		// 刷新系统参数缓存
		configService.refreshCache();
		// 刷新数据字典缓存
		dictService.refreshCache();
		// 刷新白名单缓存
		permissionUrlService.refreshCache();
		//刷新角色接口权限
		roleApiService.refreshCache();
	}

}
