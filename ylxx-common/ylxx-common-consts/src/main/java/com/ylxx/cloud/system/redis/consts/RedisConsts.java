package com.ylxx.cloud.system.redis.consts;

import com.ylxx.cloud.core.system.SystemConsts;

public class RedisConsts {

    /**
     * redis缓存key前缀
     */
    public static final String PREFIX = SystemConsts.PREFIX + "REDIS";

    public static final String PREFIX_LOCK =SystemConsts.PREFIX + "REDIS_LOCK";

    public static final String LOCK_VALUE = SystemConsts.PREFIX + "LOCK_VALUE";


}
