package com.ylxx.cloud.filter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.errorcode.SecureErrorCodeEnums;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 过滤器按照类名排序先后执行
 */
@Component
@WebFilter
@Slf4j
public class AFilterTransferEncrypt implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getRequestURI();
        if (url.endsWith(FilterConsts.defaultInitConfigUrl)) {
            chain.doFilter(req, res);
            return;
        }

        if (req.getRequestURI().contains("oaBoard/")) {
            chain.doFilter(req, res);
            return;
        }

        if (req.getRequestURI().contains("/isc-test5")) {
            chain.doFilter(req, res);
            return;
        }

        if (req.getRequestURI().contains("/isc-test6")) {
            chain.doFilter(req, res);
            return;
        }

        // 1. 判断是否为post请求
        String method = req.getMethod();
        if (!StrUtil.equalsIgnoreCase("POST", method)) {
            chain.doFilter(req, res);
            return;
        }
        // 2. 判断是否为multipart
        String contentType = req.getContentType();
        if (StrUtil.startWith(contentType, "multipart/form-data")) {
            chain.doFilter(req, res);
            return;
        }
        // 3. 判断是否启用传输加密
//        if (StrUtil.equals(ConfigConsts.VALUE_NO_0, ConfigCacheValue.SECURITY_KEY_ENABLE_TRANSFER_ENCRYPT())) {
//            chain.doFilter(req, res);
//            return;
//        }

        String bodyStr = IoUtil.read(req.getInputStream()).toString(Charset.defaultCharset());
        if (StrUtil.isNotBlank(bodyStr)) {
            bodyStr = AesUtil.decrypt(bodyStr);
            log.info("### 请求信息： {}", bodyStr);
            if (StrUtil.isBlank(bodyStr)) {
                log.error("请求体未加密");
                ApiResult result = ApiResultBuilder.failed(SecureErrorCodeEnums.SECURE_FAILED_TRANSFER_ENCRYPT.getErrorCode(), "请求体未加密");
                HttpServletUtil.responseJSONMessage(res, result);
                return;
            }
        }

        chain.doFilter(new BodyReaderHttpServletRequestWrapper(req, bodyStr.getBytes(Charset.defaultCharset())), res);
    }

    @Override
    public void destroy() {

    }

}
