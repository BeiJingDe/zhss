package com.ylxx.cloud.system.redis.util;

import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.util.SpringBeanUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具
 */
public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取所有key
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern) {
        return getTemplate().keys(pattern);
    }

    /**
     * 删除所有key
     * @param keys
     */
    public static void delete(Set<String> keys) {
        getTemplate().delete(keys);
    }

    /**
     * 删除key
     * @param key
     */
    public static void delete(String key) {
        getTemplate().delete(key);
    }

    /**
     * 存入redis，永久（系统参数永久保存）
     * @param key
     * @param value
     */
    public static void set(String key, String value) {
        getTemplate().opsForValue().set(key, value);
    }

    /**
     * 存入redis，24小时有效（用户权限一段时间后失效）
     * @param key
     * @param value
     */
    public static void set24Hour(String key, String value) {
        getTemplate().opsForValue().set(key, value, 24, TimeUnit.HOURS);
    }

    /**
     * 存入redis，超时删除
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    public static void set(String key, String value, long timeout, TimeUnit timeUnit) {
        getTemplate().opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获取key对应的值
     * @param key
     * @return
     */
    public static String get(String key) {
        Object value = getTemplate().opsForValue().get(key);
        if(value != null) {
            return value.toString();
        }
        return null;
    }
    /**
     * 2. 数据类型：Set：集合：存入redis，永久
     * @param key
     * @param value
     */
    public static void addSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }
    /**
     * 2. 数据类型：Set：集合：获取key对应的集合
     * @param key
     */
    public static Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 获取redis实例
     * @return
     */
    public static RedisTemplate<String, Object> getTemplate() {
        if(ObjectUtil.isNull(redisTemplate)) {
            redisTemplate = (RedisTemplate<String, Object>) SpringBeanUtil.getBean("redisTemplate");
        }
        return redisTemplate;
    }

}
