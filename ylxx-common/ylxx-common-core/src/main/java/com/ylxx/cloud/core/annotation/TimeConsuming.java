package com.ylxx.cloud.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: TimeConsuming 
 * @Description: 用来测试某一个方法运行的时间
 * @author: caixiaopeng
 * @date: 2018年8月8日 上午9:53:13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeConsuming {
	
	String title() default "耗时测试";
	
}
