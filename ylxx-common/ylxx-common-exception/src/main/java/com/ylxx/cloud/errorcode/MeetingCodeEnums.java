package com.ylxx.cloud.errorcode;

/**
 * 权限验证：错误码枚举
 */
public enum MeetingCodeEnums {

	AUTH_FAILED_SHIRO("AUTH_FAILED_01", "返回会议信息失败"),
	AUTH_SUCCESS_SHIRO("AUTH_SUCCESS_01", "返回会议信息成功");
	/**
	 * 错误码
	 */
	private final String errorCode;
	/**
	 * 错误提示信息
	 */
	private final String message;

	private MeetingCodeEnums(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}
	
}
