package com.ylxx.cloud.core.config;

import java.util.Collections;

//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.ylxx.cloud.core.interceptor.SecuringRestInterceptor;

/**
 * 
 * @ClassName: RestTemplateConfig 
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月11日 下午10:41:37
 */
@Configuration
public class RestTemplateConfig {
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(SecuringRestInterceptor securingRestInterceptor) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(securingRestInterceptor));
		return restTemplate;
	}
	
}
