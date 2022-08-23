package com.ylxx.cloud.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 
 * @ClassName: MultipartConfig
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月11日 下午7:50:27
 */
@Configuration
public class MultipartConfig {

	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;
	
	@Value("${spring.servlet.multipart.max-request-size}")
	private String maxRequestSize;
	
	/**
	 * @Title: multipartResolver
	 * @Description: 显示声明CommonsMultipartResolver为mutipartResolver
	 * @return
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		// resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
		resolver.setResolveLazily(true);
		resolver.setMaxInMemorySize(40960);
		resolver.setMaxUploadSize(DataSize.parse(maxRequestSize).toBytes());
		resolver.setMaxUploadSizePerFile(DataSize.parse(maxFileSize).toBytes());
		return resolver;
	}

}
