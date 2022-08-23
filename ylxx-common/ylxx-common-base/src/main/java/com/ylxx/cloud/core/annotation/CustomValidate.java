package com.ylxx.cloud.core.annotation;

import com.ylxx.cloud.core.validator.CustomValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: CustomValidate
 * @Description: 自定义校验注解
 * @author: caixiaopeng
 * @date: 2020年6月1日 下午4:36:56
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomValidator.class)
public @interface CustomValidate {

	String[] value() default { "default" };
	
	String message() default "使用自定义注解校验器CustomValidator校验失败";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	
}
