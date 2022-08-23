package com.ylxx.cloud.core.util;

import org.springframework.core.env.Environment;

import cn.hutool.core.util.ObjectUtil;

/**
 * 
* @ClassName: EnvironmentUtil
* @Description: TODO
* @author: caixiaopeng
* @date: 2020年6月21日 下午11:10:53
 */
public class EnvironmentUtil {
	
	private static Environment environment;
	
	public static String getProperty(String key) {
		return getEnvironment().getProperty(key);
	}
	
	public static String getAppName() {
		return getEnvironment().getProperty("spring.application.name");
	}
	
	private static Environment getEnvironment() {
		if(ObjectUtil.isNull(environment)) {
			environment = (Environment) SpringBeanUtil.getBean("environment");
		}
		return environment;
	}
	
}
