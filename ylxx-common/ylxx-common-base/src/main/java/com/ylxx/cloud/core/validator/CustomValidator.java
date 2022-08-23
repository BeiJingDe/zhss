package com.ylxx.cloud.core.validator;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.annotation.CustomValidate;
import com.ylxx.cloud.system.validate.util.ValidateCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 
 * @ClassName: CustomValidator 
 * @Description: 自定义注解校验器
 * @author: caixiaopeng
 * @date: 2020年6月1日 下午7:42:44
 */
@Slf4j
public class CustomValidator implements ConstraintValidator<CustomValidate, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean valid = true;
		if(StrUtil.isNotBlank(value)) {
			Object bathPath = null;
			try {
				Field field = ClassUtil.getDeclaredField(context.getClass(), "basePath");
				field.setAccessible(true);
				bathPath = field.get(context);
				field.setAccessible(false);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				log.error("获取校验属性名失败");
			}
			ConstraintValidatorContextImpl contextImpl = (ConstraintValidatorContextImpl) context;
			ConstraintDescriptor<?> descriptor = contextImpl.getConstraintDescriptor();
			Map<String, Object> attr = descriptor.getAttributes();
			String[] valueTypes = (String[]) attr.get("value");
			for(String type : valueTypes) {
				String regex = ValidateCacheUtil.getRegex(CustomValidate.class.getSimpleName(), type);
				String regexMessage = ValidateCacheUtil.getRegexMessage(CustomValidate.class.getSimpleName(), type);
				log.debug("从redis缓存中获取自定义校验正则：regex = " + regex);
				log.debug("从redis缓存中获取自定义校验正则：message = " + regexMessage);
				if(StrUtil.isBlank(regex)) {
					log.error(StrUtil.format("请检查数据库和redis缓存中是否存在validatorName={}, type={}的校验正则！", CustomValidate.class.getSimpleName(), type));
					return false;
				}
				if(!Pattern.matches(regex, value)) {
					log.error(StrUtil.format("校验失败：{} => {}", bathPath, regexMessage));
					valid = false;
				}
			}
		}
		return valid;
	}
	
}
