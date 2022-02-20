package com.club.framework.spring.controller;

import com.club.base.constant.ErrorCode;
import com.club.base.helper.ResultHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局错误配置
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Slf4j
@ControllerAdvice
public class FrameworkExceptionHandler {

    /**
     * 无请求路径
     *
     * @param e - e
     * @return java.lang.Object -
     * @author 阳春白雪 | sample@gmail.com
     * @date 2020-06-03 8:01
     * @since 1.0
     */
    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Object noHandlerFoundException(NoHandlerFoundException e) {
        log.error("没有发现访问资源：【{}】", e.getRequestURL());
        return ResultHelper.fail(ErrorCode.NO_FOUND_SERVICE);
    }

    @ResponseBody
    @ExceptionHandler
    public Object defaultException(Exception e) {
        log.error("未知错误：【{}】", e.getClass().getName(), e);
        return ResultHelper.fail(ErrorCode.UNDEFINED_ERROR);
    }
}
