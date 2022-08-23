package com.ylxx.cloud.system.redis.util;

import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.system.SystemConsts;

import java.util.Set;

/**
 * @description: 角色接口权限控制
 * @author: caixiaopeng
 * @since: 2021-11-10 16:40:53
 */
public class RoleApiCacheUtil {

    /**
     * 必要1：缓存统一key前缀
     */
    private static final String PREFIX = SystemConsts.PREFIX + "ROLE_API";

    /**
     * 必要2：获取所有key
     * @return
     */
    public static Set<String> getAllKeys() {
        return RedisUtil.keys(PREFIX + SystemConsts.CONNECTOR + "*");
    }

    /**
     * 必要3：删除所有缓存
     */
    public static void deleteCache() {
        RedisUtil.delete(getAllKeys());
    }

    // ********** ↓ 以下为具体的缓存与获取方法 ↓ ********** //

    // 给角色添加访问权限
    public static void addUrlSet(String roleCode, String appName, String method, String url) {
        if(!StrUtil.hasBlank(roleCode, appName, method, url)) {
            RedisUtil.addSet(PREFIX + SystemConsts.CONNECTOR + roleCode, appName + method + url);
        }
    }

    // 判断角色是否有访问权限
    public static boolean match(String roleCode, String appName, String method, String url) {
        if(!StrUtil.hasBlank(roleCode, appName, method, url)) {
            return RedisUtil.getTemplate().opsForSet().isMember(PREFIX + SystemConsts.CONNECTOR + roleCode, appName + method + url);
        }
        return false;
    }

}
