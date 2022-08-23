package com.ylxx.cloud.system.log.enums;

/**
 * 日志等级
 */
public enum LogLevelEnums {

    LOW("01", "低"),
    MIDDLE("02", "中"),
    HIGH("03", "高"),

    ;

    final String code;
    final String desc;

    LogLevelEnums(String code, String desc) {
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
