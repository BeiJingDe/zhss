package com.ylxx.cloud.system.log.enums;

/**
 * 日志类型
 */
public enum LogTypeEnums {

    SYSTEM("01", "系统日志"),
    BUSINESS("02", "业务日志"),

    ;

    final String code;
    final String desc;

    LogTypeEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

}
