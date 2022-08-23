package com.ylxx.cloud.shiro.realm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.errorcode.AuthErrorCodeEnums;
import com.ylxx.cloud.exception.ext.ServiceException;
import com.ylxx.cloud.shiro.token.JWTToken;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import com.ylxx.cloud.system.redis.util.RedisLockUtil;
import com.ylxx.cloud.upms.role.util.RoleCacheUtil;
import com.ylxx.cloud.upms.token.util.TokenCacheUtil;
import com.ylxx.cloud.upms.user.consts.UserConsts;
import com.ylxx.cloud.upms.user.model.UserVO;
import com.ylxx.cloud.upms.user.util.UserCacheUtil;
import com.ylxx.cloud.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName: CustomRealm
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月22日 上午10:31:57
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm {


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("————身份认证方法————");
        String token = (String) authenticationToken.getCredentials();
        // 1. 解析token，如果token有效，可以获得username
        String username = JWTUtil.getUsername(token);
        if (StrUtil.isBlank(username)) {
            throw new ServiceException(AuthErrorCodeEnums.AUTH_FAILED_SHIRO.getErrorCode(), "token认证失败，请重新登录！");
        }
        // 2. 判断token是否有在redis上注册，没有注册说明未登录
        try {
            // 获取分布式锁
            boolean tryLock = RedisLockUtil.tryLock(username);
            if (!tryLock) {
                throw new ServiceException(AuthErrorCodeEnums.AUTH_FAILED_SHIRO.getErrorCode(), "登录校验获取锁失败, 请稍后再试");
            }
            String timeMillis = TokenCacheUtil.getTimeMillis(username, token);
            log.info("Token Cache time Millis: {}", timeMillis);
            if (StrUtil.isBlank(timeMillis)) {
                throw new ServiceException(AuthErrorCodeEnums.AUTH_FAILED_SHIRO.getErrorCode(), "token已失效，请重新登录！");
            }
            // 重新注册一遍，token有效期又变回30分钟
            String sessionTimeout = ConfigCacheUtil.getConfig(ConfigConsts.GROUP_NAME_SYSTEM,
                    ConfigConsts.SYSTEM_KEY_SESSION_TIMEOUT, "30");// 默认30分钟
            TokenCacheUtil.registerToken(username, token, timeMillis, Long.parseLong(sessionTimeout), TimeUnit.MINUTES);
        } finally {
            // 释放锁
            RedisLockUtil.unlock(username);
        }
        // 3. 获取缓存中的用户信息
        UserVO user = UserCacheUtil.getUser(username);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException(AuthErrorCodeEnums.AUTH_FAILED_SHIRO.getErrorCode(), "获取不到用户信息！");
        }
        if (!StrUtil.equals(UserConsts.STATUS_TYPE_1, user.getStatus())) {
            throw new ServiceException(AuthErrorCodeEnums.AUTH_FAILED_SHIRO.getErrorCode(), "该用户账号状态异常！");
        }
        if (!JWTUtil.verify(token, user.getUsername(), user.getMmpd())) {
            throw new ServiceException(AuthErrorCodeEnums.AUTH_FAILED_SHIRO.getErrorCode(), "token认证失败，请重新登录！");
        }
        return new SimpleAuthenticationInfo(token, token, username);
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("————权限认证————");
        String username = JWTUtil.getUsername(principals.toString());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> permissions = CollUtil.newHashSet();
        List<String> roleCodes = RoleCacheUtil.getRoleCodes(username);
        if (ObjectUtil.isNotEmpty(roleCodes)) {
            permissions = roleCodes.stream().collect(Collectors.toSet());
        }
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

}
