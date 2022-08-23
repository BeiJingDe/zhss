package com.ylxx.cloud.system.config.consts;

import com.ylxx.cloud.core.system.SystemConsts;

/**
 * 
 * @ClassName: ConfigConsts 
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月11日 下午9:39:33
 */
public class ConfigConsts {
	
	/**
	 * redis缓存key前缀
	 */
	public static final String PREFIX = SystemConsts.PREFIX + "CONFIG";
	
	// 是否（0：否；1：是）
	public static final String VALUE_YES_1 = "1";
	public static final String VALUE_NO_0 = "0";

	// 1. GROUP_NAME
	public static final String GROUP_NAME_SYSTEM = "SYSTEM";
	public static final String GROUP_NAME_FILE = "FILE";
	public static final String GROUP_NAME_OSS = "OSS";
	public static final String GROUP_NAME_ISC = "ISC";
	public static final String GROUP_NAME_FDFS = "FDFS";



	// 1.1 key：是否要记录系统日志（0：否；1：是）
	public static final String SYSTEM_KEY_ENABLE_LOG = "ENABLE_LOG";
	// 1.2 key：会话失效时间（分钟）
	public static final String SYSTEM_KEY_SESSION_TIMEOUT = "SESSION_TIMEOUT";
	// 1.3 key：是否开启shiro验证（0：所有的请求都通过；1：只有允许的url名单或者token有效才能通过）
	public static final String SYSTEM_KEY_ENABLE_SHIRO = "ENABLE_SHIRO";
	// 1.4 key：token失效时间（分钟）
	public static final String SYSTEM_KEY_TOKEN_TIMEOUT = "TOKEN_TIMEOUT";
	// 1.5 key：允许同一用户同时登录的数量
	public static final String SYSTEM_KEY_SAME_USER_LOGIN_NUM = "SAME_USER_LOGIN_NUM";
	// 1.6 key：国网SM4加密密钥
	public static final String SYSTEM_KEY_SM4_KEY = "SM4_KEY";
	// 1.7 key：RSA公钥
	public static final String SYSTEM_KEY_RSA_PUBLIC_KEY = "RSA_PUBLIC_KEY";
	// 1.8 key：RSA私钥
	public static final String SYSTEM_KEY_RSA_PRIVATE_KEY = "RSA_PRIVATE_KEY";
	// 1.9 key：系统默认重置密码
	public static final String SYSTEM_KEY_DEFAULT_MMPD = "DEFAULT_MMPD";
	// 1.10 key：系统超级用户用户名
	public static final String SYSTEM_KEY_SUPER_USER = "SUPER_USER";

	// 2.1 key：文件上传存放路径
	public static final String FILE_KEY_UPLOAD_PATH = "UPLOAD_PATH";
	// 2.2 key：文件下载存放路径
	public static final String FILE_KEY_DOWNLOAD_PATH = "DOWNLOAD_PATH";
	// 2.3 key：文件存储方式（OSS：阿里云oss；FDFS：fastdfs；LOCALFS：本地文件系统）
	public static final String FILE_KEY_STORAGE_TYPE = "STORAGE_TYPE";
		// 2.3.1
		public static final String VALUE_FILE_STORAGE_TYPE_OSS = "OSS";
		// 2.3.2
		public static final String VALUE_FILE_STORAGE_TYPE_FDFS = "FDFS";
		// 2.3.3
		public static final String VALUE_FILE_STORAGE_TYPE_LOCALFS = "LOCALFS";
	// 2.4 key：是否真删除（0：否；1：是）
	public static final String FILE_KEY_IS_REAL_DELETE = "IS_REAL_DELETE";
	// 2.5 key：是否真上传（0：否；1：是）
	public static final String FILE_KEY_IS_REAL_UPLOAD = "IS_REAL_UPLOAD";
	// 2.6 key：临时文件存放目录
	public static final String FILE_KEY_TEMP_PATH = "TEMP_PATH";

	// 3.1 key：OSS访问域名
	public static final String OSS_KEY_ENDPOINT = "ENDPOINT";
	// 3.2 key：Access Key ID
	public static final String OSS_KEY_ACCESS_KEY_ID = "ACCESS_KEY_ID";
	// 3.3 key：Access Key Secret
	public static final String OSS_KEY_ACCESS_KEY_SECRET = "ACCESS_KEY_SECRET";
	// 3.4 key：Bucket是OSS上的命名空间
	public static final String OSS_KEY_BUCKET_NAME = "BUCKET_NAME";

	// 4.1 key：是否启用ISC登录（0：否；1：是）
	public static final String ISC_KEY_ENABLE = "ENABLE";
	// 4.2 key：ISC服务地址
	public static final String ISC_KEY_SERVICE = "SERVICE";
	// 4.3 key：ISC的ticket验证地址
	public static final String ISC_KEY_SERVICE_VALIDATE_URL = "SERVICE_VALIDATE_URL";
	// 4.4 key：前端ISC登录跳转URL
	public static final String ISC_KEY_LOGIN_URL = "LOGIN_URL";
	// 4.5 key：前端ISC调用注销URL
	public static final String ISC_KEY_LOGOUT_URL = "LOGOUT_URL";

	// 5.1 key：FASTDFS服务器地址，多个用逗号隔开
	public static final String FDFS_KEY_TRACKER_SERVERS = "TRACKER_SERVERS";

	public static final String IMPORTANT_CASE_APPROVAL_CONFIG = "IMPORTANT_CASE_APPROVAL_CONFIG";

	//集团填报人角色
	public static final String SYS_GROUP_COMMON	 = "SYS_GROUP_COMMON";

}
