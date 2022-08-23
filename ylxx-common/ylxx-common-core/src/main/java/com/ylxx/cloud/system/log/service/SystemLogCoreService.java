package com.ylxx.cloud.system.log.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.util.EnvironmentUtil;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.system.log.consts.SystemLogConsts;
import com.ylxx.cloud.system.log.mapper.SystemLogCoreMapper;
import com.ylxx.cloud.system.log.model.SystemLogCore;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
@Transactional
public class SystemLogCoreService {

	@Resource
	private SystemLogCoreMapper systemLogCoreMapper;

	private static String appIp;

	public void insertSystemLogCore(ProceedingJoinPoint joinPoint, Object[] args, Integer timeConsuming, Exception e) {
		String enableLog = ConfigCacheValue.SYSTEM_KEY_ENABLE_LOG();
		if(StrUtil.equals(ConfigConsts.VALUE_NO_0, enableLog)) {
			log.info(StrUtil.format("系统日志记录功能已关闭，如果要开启请将系统参数groupName={},key={}的value值设置为{}。", ConfigConsts.GROUP_NAME_SYSTEM, ConfigConsts.SYSTEM_KEY_ENABLE_LOG, ConfigConsts.VALUE_YES_1));
			return;
		}
		try {
			// 获取request
			HttpServletRequest request = HttpServletUtil.getRequest();

			// 获取日志注解
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			// 获取类上面的模块名
			LogAnnotation classAnnotation = joinPoint.getTarget().getClass().getAnnotation(LogAnnotation.class);
			LogAnnotation logAnnotation = signature.getMethod().getAnnotation(LogAnnotation.class);
			SystemLogCore systemLogCore = new SystemLogCore();
			systemLogCore.setId(IdUtil.fastSimpleUUID());
			systemLogCore.setMenuCode(HttpServletUtil.getMenuCode());
			if (classAnnotation != null) {
				systemLogCore.setLogModule(classAnnotation.module());
				systemLogCore.setLogType(classAnnotation.logType().getCode());
			}
			systemLogCore.setLogContent(logAnnotation.value());
			systemLogCore.setOperateType(logAnnotation.operateType().getValue());
			systemLogCore.setOperateResult(SystemLogConsts.OPERATE_RESULT_TYPE_1);
			if(e != null) {
				systemLogCore.setOperateResult(SystemLogConsts.OPERATE_RESULT_TYPE_0);
				systemLogCore.setExpMessage(e.getClass().getName()+" : "+e.getMessage());
			}
			systemLogCore.setCreateTime(DateUtil.date());
			systemLogCore.setCreateBy(HttpServletUtil.getUsername());
			systemLogCore.setUpdateTime(DateUtil.date());
			systemLogCore.setUpdateBy(HttpServletUtil.getUsername());
			systemLogCore.setOperateIp(HttpServletUtil.getOperateIp());
			systemLogCore.setAppName(EnvironmentUtil.getAppName());
			systemLogCore.setAppIp(getAppIp());
			systemLogCore.setAppPort(request.getServerPort());
			systemLogCore.setReqUrl(request.getRequestURI());
			systemLogCore.setReqMethod(request.getMethod());
			// 请求参数转换成json字符串长度不能超过2048
			String reqParams = JSON.toJSONString(args);
			if(StrUtil.isNotBlank(reqParams)) {
				if(reqParams.length() > 2048) {
					reqParams = reqParams.substring(0, 2048);
				}
			}
			systemLogCore.setReqParams(reqParams);
			systemLogCore.setTimeConsuming(timeConsuming);
			systemLogCore.setLogLevel(logAnnotation.logLevel().getCode());
			systemLogCoreMapper.insert(systemLogCore);
		} catch (Exception ex) {
			log.error("日志切面插入系统日志失败：" + ex.getMessage());
		}
	}

	private String getAppIp() {
		if(StrUtil.isBlank(appIp)) {
			appIp = NetUtil.getLocalhostStr();
		}
		return appIp;
	}

}
