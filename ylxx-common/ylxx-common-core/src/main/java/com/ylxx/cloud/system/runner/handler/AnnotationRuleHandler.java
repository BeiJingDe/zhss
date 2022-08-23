package com.ylxx.cloud.system.runner.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ylxx.cloud.core.annotation.CustomValidate;
import com.ylxx.cloud.system.validate.util.ValidateCacheUtil;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AnnotationRuleHandler {
	
	/**
	 * 1. CustomValidate自定义校验注解规则生成
	 */
	public static JSONArray generate(CustomValidate annotation) {
		JSONArray rules = new JSONArray();
		String[] types = annotation.value();
		for(String type : types) {
			String regex = ValidateCacheUtil.getRegex(CustomValidate.class.getSimpleName(), type);
			String message = ValidateCacheUtil.getRegexMessage(CustomValidate.class.getSimpleName(), type);
			if(StrUtil.isNotBlank(regex)) {
				JSONObject rule = new JSONObject();
				rule.put("pattern", regex);
				rule.put("message", message);
				rules.add(rule);
			}
		}
		return rules;
	}
	/**
	 * 2. NotNull校验规则生成
	 */
	public static JSONArray generate(NotNull annotation) {
		JSONArray rules = new JSONArray();
		// 暂无
		return rules;
	}
	/**
	 * 3. NotEmpty校验规则生成
	 */
	public static JSONArray generate(NotEmpty annotation) {
		JSONArray rules = new JSONArray();
		// 暂无
		return rules;
	}
	/**
	 * 4. NotBlank校验规则生成
	 */
	public static JSONArray generate(NotBlank annotation) {
		JSONArray rules = new JSONArray();
		// 暂无
		return rules;
	}
	/**
	 * 5. Size校验规则生成
	 */
	public static JSONArray generate(Size annotation) {
		JSONArray rules = new JSONArray();
		int min = annotation.min();
		int max = annotation.max();
		JSONObject rule = new JSONObject();
		rule.put("pattern", StrUtil.format(".{{},{}}", min, max));
		rule.put("message", StrUtil.format("参数长度必须在{}和{}之间", min, max));
		rules.add(rule);
		return rules;
	}
	/**
	 * 6. Range校验规则生成
	 */
	public static JSONArray generate(Range annotation) {
		JSONArray rules = new JSONArray();
		// 暂无
		return rules;
	}
	
}
