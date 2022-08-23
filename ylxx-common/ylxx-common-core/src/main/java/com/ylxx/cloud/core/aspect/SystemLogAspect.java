package com.ylxx.cloud.core.aspect;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.ylxx.cloud.system.log.service.SystemLogCoreService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class SystemLogAspect {

	@Autowired
	private SystemLogCoreService systemLogCoreService;
	
//	@AfterThrowing(value = "@annotation(com.ylxx.cloud.core.annotation.LogAnnotation)", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, Throwable e) {
		log.debug("######## afterThrowing() ########");
		log.debug(e.getMessage());
	}
	
//	@AfterReturning(value = "@annotation(com.ylxx.cloud.core.annotation.LogAnnotation)", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		log.debug("######## afterReturning() ########");
		log.debug(JSON.toJSONString(result));
	}
	
	@Around("@annotation(com.ylxx.cloud.core.annotation.LogAnnotation)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = this.cloneArgs(joinPoint.getArgs());
		
		Object result = null;
		try {
			long start = System.currentTimeMillis();
			// 执行用户请求
			result = joinPoint.proceed();
			// 计算消耗时间
			int timeConsuming = (int) (System.currentTimeMillis() - start);
			// 正常记录成功日志
			systemLogCoreService.insertSystemLogCore(joinPoint, args, timeConsuming, null);
		} catch (Exception e) {
//			// 异常记录失败日志
//			// 待定：有些异常比如参数校验异常，在这里是捕获不到的，所以异常的日志记录移到GlobalExceptionController.java
			systemLogCoreService.insertSystemLogCore(joinPoint, args, null, e);
			// 异常原样抛出不处理
			throw e;
		}
		return result;
	}
	
	private Object[] cloneArgs(Object[] args) {
		List<Object> argsList = CollUtil.newArrayList();
		for(Object arg : args) {
			if(arg instanceof ServletRequest
					|| arg instanceof ServletResponse
					|| arg instanceof MultipartFile
					|| arg instanceof MultipartFile[]) {
				// 忽略掉
			} else {
				argsList.add(ObjectUtil.clone(arg));
			}
		}
		return argsList.toArray();
	}
	
}
