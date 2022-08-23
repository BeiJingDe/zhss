package com.ylxx.cloud.core.config;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.ylxx.cloud.core.feign.MyHystrixConcurrencyStrategy;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author caixiaopeng
 * @ClassName HystrixConfig
 * Description: 自定义的hystrix线程策略
 * @date 2021-01-28 19:22:16
 */
@Configuration
public class HystrixConfig {

    @PostConstruct
    public void init() {
        // HystrixPlugins.getInstance().registerConcurrencyStrategy(new MyHystrixConcurrencyStrategy());
    }

}
