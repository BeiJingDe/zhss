package com.ylxx.cloud.shiro.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.header.HeaderConsts;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.util.EnvironmentUtil;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.errorcode.AuthErrorCodeEnums;
import com.ylxx.cloud.errorcode.SecureErrorCodeEnums;
import com.ylxx.cloud.exception.BaseException;
import com.ylxx.cloud.shiro.token.JWTToken;

import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.system.permission.util.PermissionUrlCacheUtil;
import com.ylxx.cloud.system.redis.util.RoleApiCacheUtil;
import com.ylxx.cloud.upms.role.model.RoleVO;
import com.ylxx.cloud.upms.role.util.RoleCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getRequestURI().contains("oaBoard/")) {
            return true;
        }

        // 1. 系统参数配置ENABLE_SHIRO=0，所有url无需验证
        if(StrUtil.equals(ConfigConsts.VALUE_NO_0, ConfigCacheValue.SYSTEM_KEY_ENABLE_SHIRO()) ) {
            return true;
        }
        // 2. HTTP请求白名单无需验证
        String appName = EnvironmentUtil.getAppName();
        String method = req.getMethod();
        String url = req.getRequestURI();
        if(url.startsWith("/" + appName + "/")) {
            url = url.replace("/" + appName, "");
        }
        if(PermissionUrlCacheUtil.match(appName, method, url)) {
            return true;
        }
        log.info("用户请求url：{}", url);
        // 3. 判断请求的请求头是否带上 "token"
        if (!isLoginAttempt(request, response)) {
            // 返回错误信息
            String message = "token不能为空！";
            ApiResult result = ApiResultBuilder.failed(AuthErrorCodeEnums.AUTH_FAILED_SHIRO.getErrorCode(), message);
            HttpServletUtil.responseJSONMessage(res, result);
            log.error(message);
            return false;
        }
        try {
            // 4. 如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            boolean success = executeLogin(request, response);
            if(!success) {
                return false;
            }
        } catch (Exception e) {
            // 5. token验证失败会有异常，返回错误信息到前端
            String message = "token认证失败！";
            if(e.getCause() instanceof BaseException) {
                message = e.getCause().getMessage();
            } else {
                log.error(message, e);
            }
            ApiResult result = ApiResultBuilder.failed(AuthErrorCodeEnums.AUTH_FAILED_SHIRO.getErrorCode(), message);
            HttpServletUtil.responseJSONMessage(res, result);
            return false;
        }
        // 4. 获取缓存中的角色信息，判断越权访问
        if(StrUtil.equals(ConfigConsts.VALUE_YES_1, ConfigCacheValue.SECURITY_KEY_ENABLE_ULTRA_VIRES())) {
            List<RoleVO> roleVos = RoleCacheUtil.getRoles(HttpServletUtil.getUsername());
            if(ObjectUtil.isEmpty(roleVos)) {
                String message = "越权访问，请联系管理员"; log.error(message);
                ApiResult result = ApiResultBuilder.failed(SecureErrorCodeEnums.SECURE_FAILED_ULTRA_VIRES.getErrorCode(), message);
                HttpServletUtil.responseJSONMessage(res, result);
                return false;
            }
            boolean hasPermission = false;
            for (RoleVO roleVo : roleVos) {
                if(RoleApiCacheUtil.match(roleVo.getRoleCode(), appName, method, url)) {
                    hasPermission = true;
                    break;
                }
            }
            if(!hasPermission) {
                String message = "越权访问，请联系管理员"; log.error(message);
                ApiResult result = ApiResultBuilder.failed(SecureErrorCodeEnums.SECURE_FAILED_ULTRA_VIRES.getErrorCode(), message);
                HttpServletUtil.responseJSONMessage(res, result);
                return false;
            }
        }
        return true;
    }
    /**
     * 判断用户是否想要登入。 检测 header 里面是否包含 Token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(HeaderConsts.ACCESS_TOKEN);
        if(StrUtil.isBlank(token)) {
            token = req.getParameter("accessToken");
        }
        return StrUtil.isNotBlank(token);
    }
    /**
     * 执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(HeaderConsts.ACCESS_TOKEN);
        if(StrUtil.isBlank(token)) {
            token = httpServletRequest.getParameter("accessToken");
        }
        JWTToken jwtToken = new JWTToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }
    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        //httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 这里直接重写父类方法，返回false，不然会重复执行isLoginAttempt和executeLogin方法
//		return super.onAccessDenied(request, response);
        return false;
    }
}
