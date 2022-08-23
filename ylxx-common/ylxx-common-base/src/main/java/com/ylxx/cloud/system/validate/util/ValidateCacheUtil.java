package com.ylxx.cloud.system.validate.util;

import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.system.validate.consts.ValidateConsts;

import java.util.Set;

/**
 * 
 * @ClassName: ValidateCacheUtil
 * @Description: 获取正则
 * @author: caixiaopeng
 * @date: 2020年6月5日 下午5:39:49
 */
public class ValidateCacheUtil {

	// 获取所有key
	public static Set<String> getAllKeys() {
		return RedisUtil.keys(ValidateConsts.PREFIX + SystemConsts.CONNECTOR + "*");
	}
	
	// 删除所有校验缓存
	public static void deleteCache() {
		RedisUtil.delete(getAllKeys());
	}
	
	// 添加正则缓存
	public static void setRegex(String validatorName, String type, String regex) {
		RedisUtil.set(ValidateConsts.PREFIX + SystemConsts.CONNECTOR + validatorName + SystemConsts.CONNECTOR + type, regex);
	}
	
	// 添加正则message缓存
	public static void setRegexMessage(String validatorName, String type, String message) {
		RedisUtil.set(ValidateConsts.PREFIX + SystemConsts.CONNECTOR + validatorName + SystemConsts.CONNECTOR + type + SystemConsts.CONNECTOR+"message", message);
	}
	
	// 获取正则缓存
	public static String getRegex(String validatorName, String type) {
		Object regex = RedisUtil.get(ValidateConsts.PREFIX + SystemConsts.CONNECTOR + validatorName + SystemConsts.CONNECTOR + type);
		if(ObjectUtil.isNotNull(regex)) {
			return regex.toString();
		}
		return null;
	}
	
	// 获取正则message缓存
	public static String getRegexMessage(String validatorName, String type) {
		Object message = RedisUtil.get(ValidateConsts.PREFIX + SystemConsts.CONNECTOR + validatorName + SystemConsts.CONNECTOR + type + SystemConsts.CONNECTOR+"message");
		if(ObjectUtil.isNotNull(message)) {
			return message.toString();
		}
		return null;
	}
	
}
