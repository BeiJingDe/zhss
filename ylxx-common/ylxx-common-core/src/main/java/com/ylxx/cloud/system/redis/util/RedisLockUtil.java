package com.ylxx.cloud.system.redis.util;

import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.system.redis.consts.RedisConsts;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class RedisLockUtil {

    private static final Long SUCCESS = 1L;

    private static long timeout = 9999; //获取锁的超时时间

    /**
     * 加锁，无阻塞
     */
    public static boolean tryLock(String key, String value, long expireTime) {
        long start = System.currentTimeMillis();
        for ( ; ; ) {
            //SET命令返回OK ，则证明获取锁成功
            Boolean ret = RedisUtil.getTemplate().opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
            if (ObjectUtil.equal(ret, true)) {
                return true;
            }
            //否则循环等待，在timeout时间内仍未获取到锁，则获取失败
            long end = System.currentTimeMillis() - start;
            if (end >= timeout) {
                return false;
            }
        }
    }

    /**
     * 解锁
     */
    public static boolean unlock(String key, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

        Long result = RedisUtil.getTemplate().execute(redisScript, Collections.singletonList(key), value);
        return ObjectUtil.equal(result, SUCCESS);
    }

    /**
     * 加锁3秒
     */
    public static boolean tryLock(String key) {
        return tryLock(RedisConsts.PREFIX_LOCK + SystemConsts.CONNECTOR + key, RedisConsts.LOCK_VALUE, 3);
    }
    /**
     * 解锁
     */
    public static boolean unlock(String key) {
        return unlock(RedisConsts.PREFIX_LOCK + SystemConsts.CONNECTOR + key, RedisConsts.LOCK_VALUE);
    }

}
