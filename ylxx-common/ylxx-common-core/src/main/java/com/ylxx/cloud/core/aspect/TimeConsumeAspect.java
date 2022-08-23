package com.ylxx.cloud.core.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.ylxx.cloud.core.annotation.TimeConsuming;

/**
 * 
 * @ClassName: TimeConsumeAspect 
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月11日 下午2:08:51
 */
@Aspect
@Component
public class TimeConsumeAspect {
	
	@Around("@annotation(com.ylxx.cloud.core.annotation.TimeConsuming)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long consume = System.currentTimeMillis() - start;
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		TimeConsuming timeConsuming = signature.getMethod().getAnnotation(TimeConsuming.class);
		System.out.println(timeConsuming.title() + ": " + signature + ": " + consume + "ms.");
		return result;
	}
	
}
