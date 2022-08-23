package com.ylxx.cloud.core.rest;

import java.io.Serializable;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;

import lombok.Data;

/**
 * 
 * @ClassName: ApiResult 
 * @Description: 统一API响应结果封装
 * @author: caixiaopeng
 * @date: 2020年6月11日 下午2:08:08
 */
@Data
public class ApiResult<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 成功标志
	private boolean success = true;
	// 返回数据
	private T data;
	// 错误码
	private String errorCode;
	// 提示信息
	private String message;
	// 当前时间
	private String time = DateUtil.date().toString();

	public ApiResult setSuccess(Boolean success) {
		this.success = success;
		return this;
	}

	public ApiResult setErrorCode(String errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public ApiResult setMessage(String message) {
		this.message = message;
		return this;
	}

	public ApiResult setData(T data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
