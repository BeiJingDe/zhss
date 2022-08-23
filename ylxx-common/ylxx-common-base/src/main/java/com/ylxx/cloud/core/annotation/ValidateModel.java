package com.ylxx.cloud.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: ValidateModel 
 * @Description: 该注解用在需要生成校验规则的model
 * @author: caixiaopeng
 * @date: 2020年5月16日 下午10:14:34
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateModel {
	
}
