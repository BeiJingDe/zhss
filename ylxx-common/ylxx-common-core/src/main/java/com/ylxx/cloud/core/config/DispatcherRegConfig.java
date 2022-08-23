package com.ylxx.cloud.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import cn.hutool.core.util.StrUtil;
/**
 * 
* @ClassName: DispatcherRegConfig
* @Description: TODO
* @author: caixiaopeng
* @date: 2020年6月18日 下午2:20:16
 */
@Configuration
public class DispatcherRegConfig {
	
	@Value("${spring.application.name}")
	private String appName;
	
	@Bean
	public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean<DispatcherServlet> reg = new ServletRegistrationBean<DispatcherServlet>(dispatcherServlet);
		reg.getUrlMappings().clear();
		reg.addUrlMappings("/api/*");
		reg.addUrlMappings(StrUtil.format("/{}/api/*", appName));
		reg.addUrlMappings("/*");
		return reg;
	}
	
}
