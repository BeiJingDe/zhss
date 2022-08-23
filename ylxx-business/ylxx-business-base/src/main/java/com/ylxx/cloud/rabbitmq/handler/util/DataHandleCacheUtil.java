package com.ylxx.cloud.rabbitmq.handler.util;

import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.rabbitmq.handler.consts.DataHandleConsts;
import com.ylxx.cloud.system.redis.util.RedisUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author caixiaopeng
 * @ClassName DataHandleCacheUtil
 * Description: TODO
 * @date 2021-01-05 21:22:41
 */
public class DataHandleCacheUtil {

    /**
     * IMEI：就是烤烟设备的终端编号
     */

    // 获取所有键值
    public static Set<String> getAllKeys() {
        return RedisUtil.keys(DataHandleConsts.PREFIX_IMEI + SystemConsts.CONNECTOR + "*");
    }

    // 删除所有缓存
    public static void deleteCache() {
        RedisUtil.delete(getAllKeys());
    }

    // 保存netty客户端通道ID：临时24小时
    public static void setChannelId24Hour(String IMEI, String channelId) {
        RedisUtil.set24Hour(DataHandleConsts.PREFIX_IMEI + SystemConsts.CONNECTOR + IMEI, channelId);
        RedisUtil.getTemplate().opsForSet().add(DataHandleConsts.PREFIX_CHID + SystemConsts.CONNECTOR + channelId, IMEI);
    }
    // 获取netty客户端通道ID
    public static String getChannelId(String IMEI) {
        return RedisUtil.get(DataHandleConsts.PREFIX_IMEI + SystemConsts.CONNECTOR + IMEI);
    }
    public static Set<String> getIMEISet(String channelId) {
        Set<Object> IMEISet = RedisUtil.getTemplate().opsForSet().members(DataHandleConsts.PREFIX_CHID + SystemConsts.CONNECTOR + channelId);
        if (ObjectUtil.isNotEmpty(IMEISet)) {
            return IMEISet.stream().map(item -> item.toString()).collect(Collectors.toSet());
        }
        return null;
    }
    // 删除netty客户端通道ID
    public static void deleteChannelId(String IMEI) {
        RedisUtil.delete(DataHandleConsts.PREFIX_IMEI + SystemConsts.CONNECTOR + IMEI);
    }
    public static void deleteIMEISet(String channelId) {
        RedisUtil.delete(DataHandleConsts.PREFIX_CHID + SystemConsts.CONNECTOR + channelId);
    }

    // 获取所有在线的设备编码
    public static List<String> getAllIMEI() {
        Set<String> allKeys = RedisUtil.keys(DataHandleConsts.PREFIX_IMEI + SystemConsts.CONNECTOR + "*");
        if(ObjectUtil.isNotEmpty(allKeys)) {
            return allKeys.stream().map(item -> item.split(SystemConsts.CONNECTOR)[1]).collect(Collectors.toList());
        }
        return null;
    }
    // 获取所有在线的通道ID
    public static List<String> getAllCHID() {
        Set<String> allKeys = RedisUtil.keys(DataHandleConsts.PREFIX_CHID + SystemConsts.CONNECTOR + "*");
        if(ObjectUtil.isNotEmpty(allKeys)) {
            return allKeys.stream().map(item -> item.split(SystemConsts.CONNECTOR)[1]).collect(Collectors.toList());
        }
        return null;
    }

}
