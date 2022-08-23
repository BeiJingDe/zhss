package com.ylxx.cloud.system.dict.util;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ylxx.cloud.core.system.SystemConsts;
import com.ylxx.cloud.core.util.SpringBeanUtil;
import com.ylxx.cloud.system.dict.consts.DictConsts;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Set;

public class DictCacheUtil {
	
	// 获取所有key
	public static Set<String> getAllKeys() {
		return RedisUtil.keys(DictConsts.PREFIX + SystemConsts.CONNECTOR + "*");
	}
	
	// 删除所有字典缓存
	public static void deleteCache() {
		RedisUtil.delete(getAllKeys());
	}
	/**
	 * 根据数据字典值和数据字典类型获取对应的文本值
	 *
	 * @param dictKey         数据字典类型
	 * @param defaultValue 默认值
	 * @return 返回对应的文本值
	 */
	public static String getDictLabel(String dictKey, String type,  String defaultValue) {
		if (StringUtils.isNotBlank(dictKey) && StringUtils.isNotBlank(type)) {
			Object dictMap = RedisUtil.get(DictConsts.PREFIX + SystemConsts.CONNECTOR + type);
			if(ObjectUtil.isNotNull(dictMap)) {
				JSONObject dictMapObject = JSON.parseObject(dictMap.toString());
				return (String) dictMapObject.get(dictKey);
			}
		}
		return defaultValue;
	}

	/**
	 * 根据数据字典文本值和数据字典类型获取对应的字典值
	 *
	 * @param label        数据字典文本值
	 * @param type         数据字典类型
	 * @param defaultLabel 默认值
	 * @return 返回对应的字典值
	 */
	public static String getDictValue(String label, String type, String defaultLabel) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
			String o = RedisUtil.get(DictConsts.PREFIX + SystemConsts.CONNECTOR + type);
			Map<String,Object> dictMap =JSON.parseObject(o,Map.class);
			for(Map.Entry<String, Object> entry:dictMap.entrySet()){
				if(StringUtils.equals((String)entry.getValue(),label)){
					return entry.getKey();
				}
			}
		}
		return defaultLabel;
	}
	// 添加字典缓存
	public static void setDictMap(String dictKey, String dictMap) {
		RedisUtil.set(DictConsts.PREFIX + SystemConsts.CONNECTOR + dictKey, dictMap);
	}
	
	// 获取字典缓存
	public static String getDictMap(String dictKey) {
		Object dictMap = RedisUtil.get(DictConsts.PREFIX + SystemConsts.CONNECTOR + dictKey);
		if(ObjectUtil.isNotNull(dictMap)) {
			return dictMap.toString();
		}
		return null;
	}

}
