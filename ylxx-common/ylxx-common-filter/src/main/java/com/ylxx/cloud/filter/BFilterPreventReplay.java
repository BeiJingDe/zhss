package com.ylxx.cloud.filter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.header.HeaderConsts;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.errorcode.SecureErrorCodeEnums;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.util.AesUtil;
import com.ylxx.cloud.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 过滤器按照类名排序先后执行
 */
@Component
@WebFilter
@Slf4j
public class BFilterPreventReplay implements Filter {

    private static final String PREFIX = "PREVENT_REPLAY"; // redis的key前缀

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getRequestURI();
        if(url.endsWith(FilterConsts.defaultInitConfigUrl)) {
            chain.doFilter(req, res);
            return;
        }

        // 1. 判断是否启用防重放
        if(StrUtil.equals(ConfigConsts.VALUE_NO_0, ConfigCacheValue.SECURITY_KEY_ENABLE_PREVENT_REPLAY())) {
            chain.doFilter(req, res);
            return;
        }
        // 2. 判断是否为post请求
        String method = req.getMethod();
        if(!StrUtil.equalsIgnoreCase("POST", method)) {
            chain.doFilter(req, res);
            return;
        }
        // 3. 判断是否为multipart
        String contentType = req.getContentType();
        if(StrUtil.startWith(contentType, "multipart/form-data")) {
            chain.doFilter(req, res);
            return;
        }

        String timestampStr = req.getHeader(HeaderConsts.TIMESTAMP); // 时间戳
        if(StrUtil.isBlank(timestampStr)) {
            log.error("时间戳不允许为空");
            ApiResult result = ApiResultBuilder.failed(SecureErrorCodeEnums.SECURE_FAILED_PREVENT_REPLAY.getErrorCode(), "时间戳不允许为空");
            HttpServletUtil.responseJSONMessage(res, result);
            return;
        }
        String sign = req.getHeader(HeaderConsts.SIGN); // 签名
        if(StrUtil.isBlank(sign)) {
            log.error("签名不允许为空");
            ApiResult result = ApiResultBuilder.failed(SecureErrorCodeEnums.SECURE_FAILED_PREVENT_REPLAY.getErrorCode(), "签名不允许为空");
            HttpServletUtil.responseJSONMessage(res, result);
            return;
        }

        // 校验时间
        long timeout = Long.parseLong(ConfigCacheValue.SECURITY_KEY_PREVENT_REPLAY_TIMEOUT());
        long timestamp = Long.parseLong(timestampStr);
        long currentTime = System.currentTimeMillis();
        if(currentTime - timestamp > timeout*1000) {
            log.error("时间戳校验失败");
            ApiResult result = ApiResultBuilder.failed(SecureErrorCodeEnums.SECURE_FAILED_PREVENT_REPLAY.getErrorCode(), "时间戳校验失败");
            HttpServletUtil.responseJSONMessage(res, result);
            return;
        }

        req = BodyReaderHttpServletRequestWrapper.convert(req); // 获取body之前要转换成可重复读取的request
        String bodyStr = IoUtil.read(req.getInputStream()).toString(Charset.defaultCharset());
        String token = StrUtil.blankToDefault(req.getHeader(HeaderConsts.ACCESS_TOKEN), StrUtil.blankToDefault(req.getHeader(HeaderConsts.RANDOM), ""));

        log.info("签名原始字符串：" + bodyStr+token+timestampStr);

        // 签名校验
        synchronized (StrUtil.blankToDefault(token, "default")) {
            if(this.isSignExists(sign, url)) {
                log.error("不允许重复请求");
                ApiResult result = ApiResultBuilder.failed(SecureErrorCodeEnums.SECURE_FAILED_PREVENT_REPLAY.getErrorCode(), "不允许重复请求");
                HttpServletUtil.responseJSONMessage(res, result);
                return;
            }
            // 校验签名
            String signCalc = MD5Util.toHex32(AesUtil.encrypt(bodyStr+token+timestampStr)); // 请求体+token+timestamp，加密后取MD5作为签名
            if(!StrUtil.equalsIgnoreCase(sign, signCalc)) {
                log.error("签名校验失败");
                ApiResult result = ApiResultBuilder.failed(SecureErrorCodeEnums.SECURE_FAILED_PREVENT_REPLAY.getErrorCode(), "签名校验失败");
                HttpServletUtil.responseJSONMessage(res, result);
                return;
            }
            this.setSign(sign, url);
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }

    private void setSign(String sign, String url) {
        RedisUtil.set(PREFIX + SystemConsts.CONNECTOR + sign + url, sign, Long.parseLong(ConfigCacheValue.SECURITY_KEY_PREVENT_REPLAY_TIMEOUT()), TimeUnit.SECONDS);
    }
    private boolean isSignExists(String sign, String url) {
        String s = RedisUtil.get(PREFIX + SystemConsts.CONNECTOR + sign + url);
        return StrUtil.equals(s, sign);
    }

}
