package com.ylxx.cloud.errorcode;

/**
 * 权限验证：错误码枚举
 */
public enum ErrorCodeEnums {

	PARAM_ERROR("PARAM_ERROR_01", "参数错误"),

	DATA_ERROR("DATA_ERROR", "数据处理错误"),

	REPORT_STATUS_ERROR("REPORT_STATUS_ERROR_01", "状态错误"),

	DATA_EXPIRE("DATA_EXPIRE_01", "数据内容已过期");

	/**
	 * 错误码
	 */
	private final String errorCode;
	/**
	 * 错误提示信息
	 */
	private final String message;

	private ErrorCodeEnums(String errorCode, String message) {
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
