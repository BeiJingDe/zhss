package com.ylxx.cloud.upms.user.consts;

import com.ylxx.cloud.core.system.SystemConsts;

public class UserConsts {
	
	/**
	 * redis缓存key前缀
	 */
	public static final String PREFIX = SystemConsts.PREFIX + "USER";
	
	/**
	 * 用户状态（1：正常；2：停用；3：删除）
	 */
	public static final String STATUS_TYPE_1 = "1";
	public static final String STATUS_TYPE_2 = "2";
	public static final String STATUS_TYPE_3 = "3";

	/**
	 * 性别（0：未知；1：男；2：女）
	 */
	public static final String GENDER_TYPE_0 = "0";
	public static final String GENDER_TYPE_1 = "1";
	public static final String GENDER_TYPE_2 = "2";

}
