package com.ylxx.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 微服务应用服务启动类
 * 
 * @author 26018
 *
 */

@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = { "com.ylxx.*.mapper" })
@SpringBootApplication(scanBasePackages = { "com.ylxx" })
public class ZhssVisualPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZhssVisualPlatformApplication.class, args);
	}

}
