package com.ylxx.cloud.core.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 
 * @ClassName: GlobalExceptionController
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月7日 下午2:35:03
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionController {

	/**
	 * 
	 * @Title: exceptionHandler
	 * @Description: 全局异常处理器
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ApiResult globalExceptionHandler(Exception e) {
		log.error("全局异常处理器捕获到异常：", e);
		// 自定义异常
		if (e instanceof BaseException) {
			BaseException ex = (BaseException) e;
			return ApiResultBuilder.failed(ex.getErrorCode(), ex.getMessage());
		}
		// 参数检验异常1
		if (e instanceof BindException) {
			return ApiResultBuilder.failed(StrUtil.format("参数异常：{}", this.getValidateExceptionProps(e.getMessage())));
		}
		// 参数检验异常2
		if (e instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
			return ApiResultBuilder.failed(StrUtil.format("参数异常：{}", this.getValidateExceptionProps(ex.getBindingResult().toString())));
		}
		// 调用方式错误
		if (e instanceof HttpRequestMethodNotSupportedException) {
			return ApiResultBuilder.failed("接口调用方式错误");
		}
		// 数据库唯一性约束
		if(e instanceof DuplicateKeyException) {
			return ApiResultBuilder.failed(StrUtil.format("数据唯一性约束校验失败：{}", this.getDuplicateExceptionValue(e.getMessage())));
		}
		return ApiResultBuilder.failed("未知异常：请联系管理员！");
	}

	/**
	 * 数据库唯一性约束异常
	 * @param message
	 * @return
	 */
	private String getDuplicateExceptionValue(String message) {
		if(StrUtil.isNotBlank(message)) {
			String[] ss = message.split("###");
			if(ss.length >= 2) {
				String[] ssSub = ss[1].split("'");
				if(ssSub.length >= 2) {
					return ssSub[1];
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: getBindExceptionProps
	 * @Description: 参数输入有问题，导致属性值绑定失败，解析字符串获取校验失败的属性名称
	 * @param message
	 * @return
	 */
	private List<String> getValidateExceptionProps(String message) {
		List<String> props = CollUtil.newArrayList();
		try {
			if (StrUtil.isNotBlank(message)) {
				String[] ss = message.split("\n");
				for (int i = 1; i < ss.length; i++) {
					String[] temp = ss[i].split("['\\[\\]]");
					if (temp.length > 3) {
						props.add(temp[3] + " => " + temp[temp.length - 1]);
					}
				}
			}
		} catch (Exception e) {
			log.error("获取校验异常的属性名称失败");
		}
		return props;
	}

}
