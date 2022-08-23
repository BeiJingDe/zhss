package com.ylxx.cloud.system.permission.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.SpringBeanUtil;
import com.ylxx.cloud.system.permission.consts.PermissionUrlConsts;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Set;

/**
 * 
* @ClassName: PermissionUrlUtil
* @Description: TODO
* @author: caixiaopeng
* @date: 2020年6月21日 下午5:26:28
 */
public class PermissionUrlCacheUtil {

	private static PathMatcher pathMatcher = new AntPathMatcher();
	
	public static Set<String> getAllKeys() {
		return RedisUtil.keys(PermissionUrlConsts.PREFIX + SystemConsts.CONNECTOR + "*");
	}
	
	// 删除白名单缓存
	public static void deleteCache() {
		RedisUtil.delete(getAllKeys());
	}
	
	// 添加白名单
	public static void addUrl(String appName, String method, String url) {
		if(StrUtil.isBlank(appName)) {
			appName = null;
		}
		if(StrUtil.isBlank(method)) {
			method = null;
		}
		if(StrUtil.isNotBlank(url)) {
			RedisUtil.getTemplate().opsForSet().add(PermissionUrlConsts.PREFIX + SystemConsts.CONNECTOR + appName + SystemConsts.CONNECTOR + method, url);
		}
	}
	
	// 获取白名单
	public static Set<Object> getUrls(String appName, String method) {
		if(StrUtil.isBlank(appName)) {
			appName = null;
		}
		if(StrUtil.isBlank(method)) {
			method = null;
		}
		Set<Object> urls = RedisUtil.getTemplate().opsForSet().members(PermissionUrlConsts.PREFIX + SystemConsts.CONNECTOR + appName + SystemConsts.CONNECTOR + method);
		return urls;
	}
	
	// 白名单匹配
	public static boolean match(String appName, String method, String url) {
		if(StrUtil.isBlank(appName)) {
			appName = null;
		}
		if(StrUtil.isBlank(method)) {
			method = null;
		}
		if(StrUtil.isNotBlank(url)) {
			if(RedisUtil.getTemplate().opsForSet().isMember(PermissionUrlConsts.PREFIX + SystemConsts.CONNECTOR + appName + SystemConsts.CONNECTOR + method, url) ) {
				return true;
			}
			if(RedisUtil.getTemplate().opsForSet().isMember(PermissionUrlConsts.PREFIX + SystemConsts.CONNECTOR + appName + SystemConsts.CONNECTOR + null, url) ) {
				return true;
			}
			if(RedisUtil.getTemplate().opsForSet().isMember(PermissionUrlConsts.PREFIX + SystemConsts.CONNECTOR + null + SystemConsts.CONNECTOR + null, url) ) {
				return true;
			}
			if(RedisUtil.getTemplate().opsForSet().isMember(PermissionUrlConsts.PREFIX + SystemConsts.CONNECTOR + null + SystemConsts.CONNECTOR + method, url) ) {
				return true;
			}
			Set<Object> patterns = getUrls(appName, method);
			for(Object pattern : patterns) {
				if(pathMatcher.match(pattern.toString(), url)) {
					return true;
				}
			}
			patterns = getUrls(appName, null);
			for(Object pattern : patterns) {
				if(pathMatcher.match(pattern.toString(), url)) {
					return true;
				}
			}
			patterns = getUrls(null, null);
			for(Object pattern : patterns) {
				if(pathMatcher.match(pattern.toString(), url)) {
					return true;
				}
			}
			patterns = getUrls(null, method);
			for(Object pattern : patterns) {
				if(pathMatcher.match(pattern.toString(), url)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
