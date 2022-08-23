package com.ylxx.cloud.core.rest;

import com.ylxx.cloud.errorcode.ErrorCodeEnums;

/**
 * @ClassName: ApiResultBuilder
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月23日 下午7:25:45
 */
public class ApiResultBuilder {

    // success
    public static ApiResult success() {
        return new ApiResult().setSuccess(true);
    }

    // success
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult().setSuccess(true).setData(data);
    }

    // failed
    public static ApiResult failed() {
        return new ApiResult().setSuccess(false);
    }

    // failed
    public static ApiResult failed(String message) {
        return new ApiResult().setSuccess(false).setMessage(message);
    }

    // failed
    public static ApiResult failed(String errorCode, String message) {
        return new ApiResult().setSuccess(false).setErrorCode(errorCode).setMessage(message);
    }

    // failed
    public static <T> ApiResult<T> failed(String errorCode, String message, T data) {
        return new ApiResult().setSuccess(false).setErrorCode(errorCode).setMessage(message).setData(data);
    }

    public static <T> ApiResult<T> failed(ErrorCodeEnums errorCode, T data) {
        return new ApiResult().setSuccess(false).setErrorCode(errorCode.getErrorCode()).setMessage(errorCode.getMessage()).setData(data);
    }

    public static <T> ApiResult<T> failed(ErrorCodeEnums errorCode) {
        return new ApiResult().setSuccess(false).setErrorCode(errorCode.getErrorCode()).setMessage(errorCode.getMessage());
    }
}
