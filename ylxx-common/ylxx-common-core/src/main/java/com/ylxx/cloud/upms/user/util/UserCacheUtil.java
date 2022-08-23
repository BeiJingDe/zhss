package com.ylxx.cloud.upms.user.util;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.SpringBeanUtil;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.upms.user.consts.UserConsts;
import com.ylxx.cloud.upms.user.model.UserVO;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

public class UserCacheUtil {
	
	// 获取所有key
	public static Set<String> getAllKeys() {
		return RedisUtil.keys(UserConsts.PREFIX + SystemConsts.CONNECTOR + "*");
	}
	
	// 删除所有缓存用户信息
	public static void deleteAllUsers() {
		RedisUtil.delete(getAllKeys());
	}

	// 缓存用户信息
	public static void setUser(String username, UserVO user) {
		if(ObjectUtil.isNotNull(user)) {
			RedisUtil.set24Hour(UserConsts.PREFIX + SystemConsts.CONNECTOR + username, JSON.toJSONString(user));
		}
	}
	
	// 获取用户信息
	public static UserVO getUser(String username) {
		Object user = RedisUtil.get(UserConsts.PREFIX + SystemConsts.CONNECTOR + username);
		if(ObjectUtil.isNotNull(user)) {
			return JSON.parseObject(user.toString(), UserVO.class);
		}
		return null;
	}

	// 删除用户信息
	public static void deleteUser(String username) {
		RedisUtil.delete(UserConsts.PREFIX + SystemConsts.CONNECTOR + username);
	}
	
}
