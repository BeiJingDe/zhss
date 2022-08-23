package com.ylxx.cloud.exception;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	
	public BaseException() {
		
	}
	
	public BaseException(String message) {
		super(message);
	}
	
	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BaseException(String errorCode, String message) {
		super(message);
		this.setErrorCode(errorCode);
	}
	
	public BaseException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.setErrorCode(errorCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
