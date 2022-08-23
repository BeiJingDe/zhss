package com.ylxx.cloud.upms.token.util;

import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.upms.token.consts.TokenConsts;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TokenCacheUtil {

    // 获取所有key
    public static Set<String> getAllKeys() {
        return RedisUtil.keys(TokenConsts.PREFIX + SystemConsts.CONNECTOR + "*");
    }

    // 删除所有token
    public static void deleteAllTokens() {
        RedisUtil.delete(getAllKeys());
    }

    // 获取某用户所有key
    public static Set<String> getAllKeys(String username) {
        return RedisUtil.keys(TokenConsts.PREFIX + SystemConsts.CONNECTOR + username + SystemConsts.CONNECTOR + "*");
    }

    // 删除某用户所有token
    public static void deleteAllTokens(String username) {
        RedisUtil.delete(getAllKeys(username));
    }

    // token注册
    public static void registerToken(String username, String token, String timeMillis, long timeout, TimeUnit timeUnit) {
        RedisUtil.set(TokenConsts.PREFIX + SystemConsts.CONNECTOR + username + SystemConsts.CONNECTOR + token, timeMillis, timeout, timeUnit);
    }

    // 删除token
    public static void deleteToken(String username, String token) {
        RedisUtil.delete(TokenConsts.PREFIX + SystemConsts.CONNECTOR + username + SystemConsts.CONNECTOR + token);
    }

    // 获取timeMillis
    public static String getTimeMillis(String username, String token) {
        Object timeMillis = RedisUtil.get(TokenConsts.PREFIX + SystemConsts.CONNECTOR + username + SystemConsts.CONNECTOR + token);
        if(ObjectUtil.isNotNull(timeMillis)) {
            return timeMillis.toString();
        }
        return null;
    }

}
