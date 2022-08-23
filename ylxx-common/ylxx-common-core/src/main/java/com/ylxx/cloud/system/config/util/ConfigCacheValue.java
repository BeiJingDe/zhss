package com.ylxx.cloud.system.config.util;

public class ConfigCacheValue {

//    SYSTEM	ENABLE_LOG	是否要记录系统日志（0：否；1：是）
//    SYSTEM	SESSION_TIMEOUT	会话失效时间（分钟），默认30分钟
//    SYSTEM	TOKEN_TIMEOUT	token失效时间（分钟），默认10080分钟（7天），这个时间要远大于会话失效时间
//    SYSTEM	ENABLE_SHIRO	是否开启shiro验证（0：否；1：只有白名单或者token有效才能通过）
//    SYSTEM	SAME_USER_LOGIN_NUM	允许同一用户同时登录的数量，默认1个（单点登录）
//    SYSTEM	SM4_KEY	国网SM4加密密钥
//    SYSTEM	RSA_PUBLIC_KEY	RSA公钥
//    SYSTEM	RSA_PRIVATE_KEY	RSA私钥
//    SYSTEM	DEFAULT_MMPD	系统默认重置密码
//    SYSTEM	SUPER_USER	系统超级用户用户名
//    FILE	UPLOAD_PATH	文件上传存放路径
//    FILE	DOWNLOAD_PATH	文件下载存放路径
//    FILE	STORAGE_TYPE	文件存储方式（OSS：阿里云oss；FDFS：fastdfs；LOCALFS：本地文件系统）
//    FILE	IS_REAL_DELETE	是否真删除（0：否；1：是）
//    FILE	IS_REAL_UPLOAD	是否真上传（0：否；1：是）
//    OSS	ENDPOINT	OSS访问域名
//    OSS	ACCESS_KEY_ID	Access Key ID
//    OSS	ACCESS_KEY_SECRET	Access Key Secret
//    OSS	BUCKET_NAME	Bucket是OSS上的命名空间
//    ISC	ENABLE	是否启用ISC登录（0：否；1：是）
//    ISC	SERVICE	ISC服务地址
//    ISC	SERVICE_VALIDATE_URL	ISC的ticket验证地址
//    ISC	LOGIN_URL	前端ISC登录跳转URL
//    ISC	LOGOUT_URL	前端ISC调用注销URL
//    FDFS	TRACKER_SERVERS	FASTDFS服务器地址，多个用逗号隔开

    // 系统相关参数
    public static String SYSTEM_KEY_REFRESH_FLAG()            { return ConfigCacheUtil.getConfig("SYSTEM", "REFRESH_FLAG"); }
    public static String SYSTEM_KEY_ENABLE_LOG()            { return ConfigCacheUtil.getConfig("SYSTEM", "ENABLE_LOG"); }
    public static String SYSTEM_KEY_SESSION_TIMEOUT()       { return ConfigCacheUtil.getConfig("SYSTEM", "SESSION_TIMEOUT"); }
    public static String SYSTEM_KEY_TOKEN_TIMEOUT()         { return ConfigCacheUtil.getConfig("SYSTEM", "TOKEN_TIMEOUT"); }
    public static String SYSTEM_KEY_ENABLE_SHIRO()          { return ConfigCacheUtil.getConfig("SYSTEM", "ENABLE_SHIRO"); }
    public static String SYSTEM_KEY_SAME_USER_LOGIN_NUM()   { return ConfigCacheUtil.getConfig("SYSTEM", "SAME_USER_LOGIN_NUM"); }
    public static String SYSTEM_KEY_SM4_KEY()               { return ConfigCacheUtil.getConfig("SYSTEM", "SM4_KEY"); }
    public static String SYSTEM_KEY_RSA_PUBLIC_KEY()        { return ConfigCacheUtil.getConfig("SYSTEM", "RSA_PUBLIC_KEY"); }
    public static String SYSTEM_KEY_RSA_PRIVATE_KEY()       { return ConfigCacheUtil.getConfig("SYSTEM", "RSA_PRIVATE_KEY"); }
    public static String SYSTEM_KEY_DEFAULT_MMPD()          { return ConfigCacheUtil.getConfig("SYSTEM", "DEFAULT_MMPD"); }
    public static String SYSTEM_KEY_SUPER_ADMIN()           { return ConfigCacheUtil.getConfig("SYSTEM", "SUPER_ADMIN"); }
    public static String SYSTEM_KEY_IS_USER_REAL_DELETE()   { return ConfigCacheUtil.getConfig("SYSTEM", "IS_USER_REAL_DELETE"); }
    public static String SYSTEM_KEY_IS_DEPLOY_PROD()        { return ConfigCacheUtil.getConfig("SYSTEM", "IS_DEPLOY_PROD"); }
    public static String SYSTEM_KEY_DEFAULT_ROLE()          { return ConfigCacheUtil.getConfig("SYSTEM", "DEFAULT_ROLE"); }
    public static String SYSTEM_KEY_FRONT_DEPLOY_ADDRESS()          { return ConfigCacheUtil.getConfig("SYSTEM", "FRONT_DEPLOY_ADDRESS"); }
    public static String SYSTEM_KEY_LOG_BACKUP_THRESHOLD_CAPACITY()         { return ConfigCacheUtil.getConfig("SYSTEM", "LOG_BACKUP_THRESHOLD_CAPACITY"); }
    public static String SYSTEM_KEY_LOG_BACKUP_RETAIN_DAY()                 { return ConfigCacheUtil.getConfig("SYSTEM", "LOG_BACKUP_RETAIN_DAY"); }

    // 安全功能
    public static String SECURITY_KEY_ENABLE_SECURITY()             { return ConfigCacheUtil.getConfig("SECURITY", "ENABLE_SECURITY", "1"); }
    public static String SECURITY_KEY_ENABLE_TRANSFER_ENCRYPT()     { return ConfigCacheUtil.getConfig("SECURITY", "ENABLE_TRANSFER_ENCRYPT", "1"); }
    public static String SECURITY_KEY_ENABLE_PREVENT_REPLAY()       { return ConfigCacheUtil.getConfig("SECURITY", "ENABLE_PREVENT_REPLAY", "1"); }
    public static String SECURITY_KEY_PREVENT_REPLAY_TIMEOUT()      { return ConfigCacheUtil.getConfig("SECURITY", "PREVENT_REPLAY_TIMEOUT", "60"); }
    public static String SECURITY_KEY_ENABLE_ULTRA_VIRES()          { return ConfigCacheUtil.getConfig("SECURITY", "ENABLE_ULTRA_VIRES", "60"); }


    // 文件上传相关参数
    public static String FILE_KEY_UPLOAD_PATH()     { return ConfigCacheUtil.getConfig("FILE", "UPLOAD_PATH"); }
    public static String FILE_KEY_DOWNLOAD_PATH()   { return ConfigCacheUtil.getConfig("FILE", "DOWNLOAD_PATH"); }
    public static String FILE_KEY_STORAGE_TYPE()    { return ConfigCacheUtil.getConfig("FILE", "STORAGE_TYPE"); }
    public static String FILE_KEY_IS_REAL_DELETE()  { return ConfigCacheUtil.getConfig("FILE", "IS_REAL_DELETE"); }
    public static String FILE_KEY_IS_REAL_UPLOAD()  { return ConfigCacheUtil.getConfig("FILE", "IS_REAL_UPLOAD"); }

    // 阿里OSS配置相关参数
    public static String OSS_KEY_ENDPOINT()             { return ConfigCacheUtil.getConfig("OSS", "ENDPOINT"); }
    public static String OSS_KEY_ACCESS_KEY_ID()        { return ConfigCacheUtil.getConfig("OSS", "ACCESS_KEY_ID"); }
    public static String OSS_KEY_ACCESS_KEY_SECRET()    { return ConfigCacheUtil.getConfig("OSS", "ACCESS_KEY_SECRET"); }
    public static String OSS_KEY_BUCKET_NAME()          { return ConfigCacheUtil.getConfig("OSS", "BUCKET_NAME"); }

    // ISC权限相关参数
    public static String ISC_KEY_ENABLE()               { return ConfigCacheUtil.getConfig("ISC", "ENABLE"); }
    public static String ISC_KEY_SERVICE()              { return ConfigCacheUtil.getConfig("ISC", "SERVICE"); }
    public static String ISC_KEY_SERVICE_VALIDATE_URL() { return ConfigCacheUtil.getConfig("ISC", "SERVICE_VALIDATE_URL"); }
    public static String ISC_KEY_LOGIN_URL()            { return ConfigCacheUtil.getConfig("ISC", "LOGIN_URL"); }
    public static String ISC_KEY_LOGOUT_URL()           { return ConfigCacheUtil.getConfig("ISC", "LOGOUT_URL"); }

    // FastDFS文件系统相关参数
    public static String FDFS_KEY_TRACKER_SERVERS()     { return ConfigCacheUtil.getConfig("FDFS", "TRACKER_SERVERS"); }

    // 气象数据采集相关参数
    public static String WEATHER_KEY_DATA_COLLECT_URL()     { return ConfigCacheUtil.getConfig("WEATHER", "DATA_COLLECT_URL"); }
    public static String WEATHER_KEY_REFERER()     { return ConfigCacheUtil.getConfig("WEATHER", "REFERER"); }
    public static String WEATHER_KEY_ENABLE_RESULT_LOG()     { return ConfigCacheUtil.getConfig("WEATHER", "ENABLE_RESULT_LOG"); }

}
