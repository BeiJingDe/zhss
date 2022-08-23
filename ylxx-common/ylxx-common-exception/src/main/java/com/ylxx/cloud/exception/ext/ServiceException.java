package com.ylxx.cloud.exception.ext;

import com.ylxx.cloud.exception.BaseException;

/**
 * service异常
 * @author caixiaopeng
 *
 */
public class ServiceException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public ServiceException() {
		
	}
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ServiceException(String errorCode, String message) {
		super(errorCode, message);
	}
	
	public ServiceException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
	
}
