package com.ylxx.cloud.core.annotation;

import com.ylxx.cloud.system.log.enums.LogLevelEnums;
import com.ylxx.cloud.system.log.enums.LogTypeEnums;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @ClassName: LogAnnotation
 * @Description: 用于记录日志
 * @author: caixiaopeng
 * @date: 2020年4月5日 下午11:22:30
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAnnotation {

	String value() default "查询";

	String module() default "";

	OperateTypeEnum operateType() default OperateTypeEnum.QUERY;

	LogTypeEnums logType() default LogTypeEnums.BUSINESS; // 日志类型

	LogLevelEnums logLevel() default LogLevelEnums.LOW; // 日志等级

}
