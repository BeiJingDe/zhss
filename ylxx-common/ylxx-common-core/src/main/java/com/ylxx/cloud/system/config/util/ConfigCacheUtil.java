package com.ylxx.cloud.system.config.util;

import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.SpringBeanUtil;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

/**
 * 
 * @ClassName: ConfigUtil 
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月11日 下午9:10:53
 */
public class ConfigCacheUtil {
	
	public static Set<String> getAllKeys() {
		return RedisUtil.keys(ConfigConsts.PREFIX + SystemConsts.CONNECTOR + "*");
	}
	
	public static void deleteCache() {
		RedisUtil.delete(getAllKeys());
	}
	
	public static void setConfig(String configGroup, String key, String value) {
		RedisUtil.set(ConfigConsts.PREFIX + SystemConsts.CONNECTOR + configGroup + SystemConsts.CONNECTOR + key, value);
	}
	
	public static String getConfig(String configGroup, String key) {
		Object value = RedisUtil.get(ConfigConsts.PREFIX + SystemConsts.CONNECTOR + configGroup + SystemConsts.CONNECTOR + key);
		if(ObjectUtil.isNotNull(value)) {
			return value.toString();
		}
		return null;
	}

	public static String getConfig(String configGroup, String key, String defaultValue) {
		Object value = RedisUtil.get(ConfigConsts.PREFIX + SystemConsts.CONNECTOR + configGroup + SystemConsts.CONNECTOR + key);
		if(ObjectUtil.isNotNull(value)) {
			return value.toString();
		}
		return defaultValue;
	}
	
}
