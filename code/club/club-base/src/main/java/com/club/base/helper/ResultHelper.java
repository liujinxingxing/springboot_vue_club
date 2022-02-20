package com.club.base.helper;


import com.club.base.bean.ResultMessage;
import com.club.base.bean.Result;
import com.club.base.constant.ErrorCode;
import com.club.base.exception.FrameworkException;

/**
 * 结果返回值
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class ResultHelper {

    public static Result<String> get(boolean success) {
        return new Result<String>(success);
    }

    public static <T> Result<T> success(T value) {
        return new Result<T>(value);
    }

    public static <T> Result<T> success(T value, boolean next) {
        Result<T> result = new Result<>(value);
        result.setNext(next);
        return result;
    }

    public static <T> Result<T> success() {
        return new Result<T>(true);
    }

    public static <T> Result<T> success(int code, String message) {
        return new Result<T>(true, code, message, null);
    }

    public static <T> Result<T> fail(int errCode, String message) {
        return new Result<T>(false, errCode, message, null);
    }

    public static <T> Result<T> fail(int errCode, String message, Object info) {
        Result<T> ret = fail(errCode, message);
        ret.setInfo(info);
        return ret;
    }

    public static <T> Result<T> fail(int errCode) {
        return new Result<T>(false, errCode, null);
    }

    public static <T> Result<T> fail(ResultMessage errCode) {
        return fail(errCode.getCode(), errCode.getMessage());
    }

    public static <T> Result<T> fail(ResultMessage errCode, Object info) {
        return fail(errCode.getCode(), errCode.getMessage(), info);
    }

    public static <T> Result<T> fail() {
        return new Result<T>(false, ErrorCode.UNDEFINED_ERROR, null);
    }

    public static <T> Result<T> fail(FrameworkException e) {
        return new Result<T>(false, e.getCode(), e.getMessage(), null);
    }

}

