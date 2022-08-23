package com.ylxx.cloud.core.feign;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.Callable;

/**
 * @author caixiaopeng
 * @ClassName HystrixConcurrencyStrategy
 * Description:
 *      开启hystrix后，feign请求，会运行在hystrix管理的另一线程下，所以RequestContextHolder.currentRequestAttributes()无法获取值。
 *      解决方法：创建一个自定义的hystrix 线程策略, 将servletRequestAttributes传入新线程中，并赋给RequestContextHolder
 * @date 2021-01-28 19:17:50
 */
@Slf4j
public class MyHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        ServletRequestAttributes servletRequestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return new Callable<T>() {
            @Override
            public T call() throws Exception {
                try {
                    if (null != servletRequestAttributes) {
                        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
                    }
                    return callable.call();
                }finally {
                    RequestContextHolder.resetRequestAttributes();
                }
            }
        };
    }

}
