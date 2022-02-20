package com.club.base.exception;


import com.club.base.bean.ResultMessage;

/**
 * ErrorCodeException的描述
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class FrameworkException extends RuntimeException implements ResultMessage {

    private static final long serialVersionUID = 4143519479094905222L;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误消息
     */
    private String message;

    public FrameworkException(String message) {
        this.message = message;
    }

    public FrameworkException(ResultMessage errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public FrameworkException() {
    }

    public FrameworkException(int code) {
        this.code = code;
    }

    public FrameworkException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
