package com.ylxx.cloud.core.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.exception.ext.ServiceException;

/**
 * @description: 断言类
 * @author: caixiaopeng
 * @since: 2021-08-29 17:03:42
 */
public final class Assert {

    /**
     * value不是true抛异常
     */
    public static void isTrue(Boolean value, String message, Object... params) {
        if(ObjectUtil.isNull(value) || value == false) {
            throw new ServiceException(StrUtil.format(message, params));
        }
    }

    /**
     * value不是false抛异常
     */
    public static void isFalse(Boolean value, String message, Object... params) {
        if(ObjectUtil.isNull(value) || value == true) {
            throw new ServiceException(StrUtil.format(message, params));
        }
    }

    /**
     * value为null抛异常
     */
    public static void notNull(Object value, String message, Object... params) {
        if(ObjectUtil.isNull(value)) {
            throw new ServiceException(StrUtil.format(message, params));
        }
    }

    /**
     * str为空抛异常
     */
    public static void notBlank(String str, String message, Object... params) {
        if(StrUtil.isBlank(str)) {
            throw new ServiceException(StrUtil.format(message, params));
        }
    }

    /**
     * obj为空抛异常
     */
    public static void notEmpty(Object obj, String message, Object... params) {
        if(ObjectUtil.isEmpty(obj)) {
            throw new ServiceException(StrUtil.format(message, params));
        }
    }

}
