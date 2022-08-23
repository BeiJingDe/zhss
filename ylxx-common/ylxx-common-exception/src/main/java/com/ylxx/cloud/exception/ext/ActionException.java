package com.ylxx.cloud.exception.ext;

import com.ylxx.cloud.exception.BaseException;

/**
 * controller异常
 * @author caixiaopeng
 *
 */
public class ActionException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public ActionException() {
		
	}
	
	public ActionException(String message) {
		super(message);
	}
	
	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ActionException(String errorCode, String message) {
		super(errorCode, message);
	}
	
	public ActionException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
	
}
