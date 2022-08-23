package com.ylxx.cloud.core.feign;

import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author caixiaopeng
 * @ClassName FeignServiceFallback
 * Description: Feign回调
 * @date 2021-01-28 17:40:50
 */
@Slf4j
@Component
public class FeignServiceFallback implements FallbackFactory<ApiResult> {

    @Override
    public ApiResult create(Throwable cause) {
        return FeignServiceFallback.failed(cause);
    }

    /**
     * 服务降级
     * @param cause
     * @return
     */
    public static ApiResult failed(Throwable cause) {
        log.error("服务降级", cause);
        return ApiResultBuilder.failed("服务降级");
    }

}
