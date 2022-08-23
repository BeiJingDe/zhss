package com.ylxx.cloud.errorcode;

/**
 * 权限验证：错误码枚举
 */
public enum SecureErrorCodeEnums {

	SECURE_FAILED_TRANSFER_ENCRYPT("SECURE_FAILED_01", "传输加密验证失败"),
	SECURE_FAILED_PREVENT_REPLAY("SECURE_FAILED_02", "防重放验证失败"),
	SECURE_FAILED_ULTRA_VIRES("SECURE_FAILED_03", "越权访问验证失败"),

	;

	/**
	 * 错误码
	 */
	private final String errorCode;
	/**
	 * 错误提示信息
	 */
	private final String message;

	SecureErrorCodeEnums(String errorCode, String message) {
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
