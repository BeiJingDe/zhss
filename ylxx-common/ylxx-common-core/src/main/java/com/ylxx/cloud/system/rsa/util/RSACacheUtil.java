package com.ylxx.cloud.system.rsa.util;

import cn.hutool.core.util.ObjectUtil;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.SpringBeanUtil;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.system.rsa.consts.RSAConsts;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

public class RSACacheUtil {

	public static Set<String> getAllKeys() {
		return RedisUtil.keys(RSAConsts.PREFIX + SystemConsts.CONNECTOR + "*");
	}
	
	// 删除RSA公私钥缓存
	public static void deleteCache() {
		RedisUtil.delete(getAllKeys());
	}
	
	public static void setPublicKey(String publicKey) {
		RedisUtil.set(RSAConsts.PREFIX + SystemConsts.CONNECTOR + RSAConsts.PUBLIC_KEY, publicKey);
	}
	
	public static void setPrivateKey(String privateKey) {
		RedisUtil.set(RSAConsts.PREFIX + SystemConsts.CONNECTOR + RSAConsts.PRIVATE_KEY, privateKey);
	}
	
	public static String getPublicKey() {
		Object publicKey = RedisUtil.get(RSAConsts.PREFIX + SystemConsts.CONNECTOR + RSAConsts.PUBLIC_KEY);
		if(ObjectUtil.isNotNull(publicKey)) {
			return publicKey.toString();
		}
		return null;
	}
	
	public static String getPrivateKey() {
		Object privateKey = RedisUtil.get(RSAConsts.PREFIX + SystemConsts.CONNECTOR + RSAConsts.PRIVATE_KEY);
		if(ObjectUtil.isNotNull(privateKey)) {
			return privateKey.toString();
		}
		return null;
	}
	
}
