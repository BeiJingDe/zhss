package com.ylxx.cloud.upms.org.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.upms.org.consts.OrgConsts;

import java.util.List;

/**
 * 组织信息缓存管理
 */
public class OrgCacheUtil {

    public static void setOrgCodes(String username, List<String> orgCodes) {
        RedisUtil.set24Hour(OrgConsts.PREFIX + SystemConsts.CONNECTOR + username, CollUtil.join(orgCodes, ","));
    }

    public static List<String> getOrgCodes(String username) {
        String orgCodes = RedisUtil.get(OrgConsts.PREFIX + SystemConsts.CONNECTOR + username);
        if(StrUtil.isNotBlank(orgCodes)) {
            return CollUtil.newArrayList(orgCodes.split(","));
        }
        return null;
    }

}
