package com.ylxx.cloud.errorcode;

/**
 * 权限验证：错误码枚举
 */
public enum AuthErrorCodeEnums {
	
	AUTH_FAILED_SHIRO("AUTH_FAILED_01", "权限验证失败");
	
	/**
	 * 错误码
	 */
	private final String errorCode;
	/**
	 * 错误提示信息
	 */
	private final String message;
	
	private AuthErrorCodeEnums(String errorCode, String message) {
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
