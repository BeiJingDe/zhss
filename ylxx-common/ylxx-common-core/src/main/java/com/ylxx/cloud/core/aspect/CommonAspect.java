package com.ylxx.cloud.core.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @ClassName: CommonAspect 
 * @Description: 切面示例
 * @author: caixiaopeng
 * @date: 2018年8月8日 下午4:54:17
 * @Warning: 【注】切面类必须是spring bean
 */
//@Aspect
//@Component
public class CommonAspect {
	
	@Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	public void before(JoinPoint joinPoint) {
		System.out.println("######## before() ########");
	}
	@After("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	public void after(JoinPoint joinPoint) {
		System.out.println("######## after() ########");
	}
	@Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("######## around() before ########");
		System.out.println("===参数===");
		Object[] args = joinPoint.getArgs();
		for(Object obj : args) {
			System.out.println(obj);
		}
		System.out.println("===参数===");
		System.out.println("===运行过程===");
		Object result = joinPoint.proceed();
		System.out.println("===运行过程===");
		System.out.println("===返回结果===");
		System.out.println(JSON.toJSONString(result));
		System.out.println("===返回结果===");
		System.out.println("######## around() after ########");
		return result;
	}
	@AfterThrowing(value = "@annotation(org.springframework.web.bind.annotation.GetMapping)", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, Throwable e) {
		System.out.println("######## afterThrowing() ########");
		System.out.println(e.getMessage());
	}
	@AfterReturning(value = "@annotation(org.springframework.web.bind.annotation.GetMapping)", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		System.out.println("######## afterReturning() ########");
		System.out.println(JSON.toJSONString(result));
	}
	
}
