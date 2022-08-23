package com.ylxx.cloud.system.isc.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.system.isc.consts.IscConsts;
import com.ylxx.cloud.system.redis.util.RedisUtil;

import java.util.List;
import java.util.Set;

public class IscCacheUtil {

    // 获取所有key
    public static Set<String> getAllKeys() {
        return RedisUtil.keys(IscConsts.PREFIX + SystemConsts.CONNECTOR + "*");
    }

    // 删除所有字典缓存
    public static void deleteCache() {
        RedisUtil.delete(getAllKeys());
    }

    public static void setPermission(String key, List<String> permission) {
        RedisUtil.set(IscConsts.PREFIX + SystemConsts.CONNECTOR + key + SystemConsts.CONNECTOR + HttpServletUtil.getUsername(),
                CollUtil.join(permission, ","));
    }

    public static void delPermission(String key) {
        RedisUtil.delete(IscConsts.PREFIX + SystemConsts.CONNECTOR + key + SystemConsts.CONNECTOR + HttpServletUtil.getUsername());
    }

    public static String getPermission(String key) {
        Object value = RedisUtil.get(IscConsts.PREFIX + SystemConsts.CONNECTOR + key + SystemConsts.CONNECTOR + HttpServletUtil.getUsername());

        if (ObjectUtil.isNotNull(value)) {
            return value.toString();
        }
        return null;
    }

}
