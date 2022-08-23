package com.ylxx.cloud.system.validate.util;

import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.system.validate.consts.ValidateConsts;

import java.util.Set;

/**
 * 
 * @ClassName: ValidateModelUtil 
 * @Description: 获取前后端一致校验model
 * @author: caixiaopeng
 * @date: 2020年6月5日 下午5:39:49
 */
public class ValidateModelCacheUtil {
	
	// 获取所有key
	public static Set<String> getAllKeys() {
		return RedisUtil.keys(ValidateConsts.PREFIX_MODEL + SystemConsts.CONNECTOR + "*");
	}
	
	// 删除所有校验model缓存
	public static void deleteCache() {
		RedisUtil.delete(getAllKeys());
	}
	
	// 获取指定微服务所有key
	public static Set<String> getAllKeys(String appName) {
		return RedisUtil.keys(ValidateConsts.PREFIX_MODEL + SystemConsts.CONNECTOR + appName + SystemConsts.CONNECTOR + "*");
	}
	
	// 删除指定微服务校验model缓存
	public static void deleteCache(String appName) {
		RedisUtil.delete(getAllKeys(appName));
	}
	
	// 添加一致校验model缓存
	public static void setModelRule(String appName, String modelName, String modelRule) {
		RedisUtil.set(ValidateConsts.PREFIX_MODEL + SystemConsts.CONNECTOR + appName + SystemConsts.CONNECTOR + modelName, modelRule);
	}
	
	// 获取一致校验model缓存
	public static String getModelRule(String appName, String modelName) {
		Object modelRule = RedisUtil.get(ValidateConsts.PREFIX_MODEL + SystemConsts.CONNECTOR + appName + SystemConsts.CONNECTOR + modelName);
		if(ObjectUtil.isNotNull(modelRule)) {
			return modelRule.toString();
		}
		return null;
	}
	
}
