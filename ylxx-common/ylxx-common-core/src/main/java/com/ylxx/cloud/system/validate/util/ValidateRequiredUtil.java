package com.ylxx.cloud.system.validate.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ylxx.cloud.exception.ext.ActionException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 校验参数必填项
 * 
 * @author caixiaopeng
 *
 */
@Slf4j
public class ValidateRequiredUtil {

	/**
	 * 说明：目前只校验 1. @NotNull 2. @NotEmpty 3. @NotBlank
	 * 
	 * @param clazz：校验分组
	 * @param object：校验对象
	 */
	public static void validate(Class<?> validateGroupClass, Object object) {
		// 获取所有父类
		Class<?> temp = object.getClass();
		List<Class<?>> models = CollUtil.newArrayList();
		while (!StrUtil.equals(temp.getName(), Object.class.getName())) {
			models.add(temp);
			temp = temp.getSuperclass();
		}
		models = CollUtil.reverse(models);
		// 获取所有属性
		Map<String, Field> fieldMap = CollUtil.newHashMap();
		Map<String, Object> fieldValudMap = CollUtil.newHashMap();
		for (Class<?> model : models) {
			Field[] fields = model.getDeclaredFields();
			for (Field field : fields) {
				if (!Modifier.isStatic(field.getModifiers())) {
					boolean isAccessible = field.isAccessible();
					if(!isAccessible) {
						field.setAccessible(true);
					}
					// 获取属性
					String fieldName = field.getName();
					// 获取属性值
					Object fieldValue = null;
					try {
						fieldValue = field.get(object);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						log.error("获取属性值失败", e);
					}
					
					fieldMap.put(fieldName, field);
					fieldValudMap.put(fieldName, fieldValue);
					
					if(!isAccessible) {
						field.setAccessible(false);
					}
				}
			}
		}
		// 封装错误提示信息
		List<String> errMsgs = CollUtil.newArrayList(); // 开发查看错误信息
		fieldMap.forEach((fieldName, field) -> {
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof NotNull) {
					NotNull tempAnno = (NotNull) annotation;
					Class<?>[] groups = tempAnno.groups();
					String message = tempAnno.message();
					for (Class<?> clazz : groups) {
						if(StrUtil.equals(clazz.getName(), validateGroupClass.getName())) {
							if(ObjectUtil.isNull(fieldValudMap.get(fieldName))) {
								errMsgs.add(StrUtil.format("{}=>{}", fieldName, message));
							}
						}
					}
				}
				if (annotation instanceof NotEmpty) {
					NotEmpty tempAnno = (NotEmpty) annotation;
					Class<?>[] groups = tempAnno.groups();
					String message = tempAnno.message();
					for (Class<?> clazz : groups) {
						if(StrUtil.equals(clazz.getName(), validateGroupClass.getName())) {
							if(ObjectUtil.isEmpty(fieldValudMap.get(fieldName))) {
								errMsgs.add(StrUtil.format("{}=>{}", fieldName, message));
							}
						}
					}
				}
				if (annotation instanceof NotBlank) {
					NotBlank tempAnno = (NotBlank) annotation;
					Class<?>[] groups = tempAnno.groups();
					String message = tempAnno.message();
					for (Class<?> clazz : groups) {
						if(StrUtil.equals(clazz.getName(), validateGroupClass.getName())) {
							String fieldValue = (String) fieldValudMap.get(fieldName);
							if(StrUtil.isBlank(fieldValue)) {
								errMsgs.add(StrUtil.format("{}=>{}", fieldName, message));
							}
						}
					}
				}
			}
		});
		if (ObjectUtil.isNotEmpty(errMsgs)) {
			throw new ActionException(CollUtil.join(errMsgs, "，"));
		}
	}

}
