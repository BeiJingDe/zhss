package com.ylxx.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 微服务应用服务启动类
 * 
 * @author 26018
 *
 */
@MapperScan(basePackages = { "com.ylxx.**.mapper" })
@SpringBootApplication(scanBasePackages = { "com.ylxx" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
