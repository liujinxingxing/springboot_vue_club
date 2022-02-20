package com.club.framework.spring.controller;

import com.alibaba.fastjson.JSON;
import com.club.framework.spring.util.UserUtil;
import com.club.base.bean.User;
import com.club.base.constant.ErrorCode;
import com.club.base.constant.StringPool;
import com.club.base.exception.FrameworkException;
import com.club.base.helper.ResultHelper;
import com.club.base.util.StringUtil;
import com.club.framework.valid.vo.FieldErrorVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * BaseController的描述
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class BaseController {

    @JsonIgnore
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    @RequestMapping
    public ModelAndView index() {
        String ctxPath = request.getContextPath();
        String uri = request.getRequestURI();
        int tok = uri.indexOf(StringPool.COOKIES_SPLIT);
        int end = uri.length();
        if (tok >= 0) {
            end = tok;
        }
        uri = uri.substring(ctxPath.length(), end);
        if (!uri.endsWith(StringPool.PATHSPLIT)) {
            uri += StringPool.PATHSPLIT;
        }
        uri += StringPool.URL_INDEX;
        return new ModelAndView(uri);
    }


    /**
     * 获取登录用户
     * @return com.daysh.base.bean.User - 登录用户
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    protected User getUser(){
        return UserUtil.getRequestUser(request);
    }

    /**
     * methodArgumentNotValidException
     * 参数检验失败
     * @param e -
     * @return java.lang.Object -
     * @author 阳春白雪 | sample@gmail.com
     * @date 2020-06-03 7:57
     * @since 1.0
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object methodArgumentNotValidException(MethodArgumentNotValidException e) {
        error();
        List<FieldErrorVo> modelFieldErrors = new ArrayList<FieldErrorVo>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            modelFieldErrors
                    .add(new FieldErrorVo(fieldError.getField(), fieldError.getCode(), fieldError.getRejectedValue()));
        }
        log.error("请求参数校验失败:{}", JSON.toJSONString(modelFieldErrors));
        return ResultHelper.fail(ErrorCode.REQ_PARAM_ERROR,modelFieldErrors);
    }

    @ResponseBody
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public Object httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        error();
        return ResultHelper.fail(ErrorCode.REQ_PARAM_ERROR.getCode(),String.format("不支持的头部类型：%s",e.getContentType().toString()),e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Object httpMessageNotReadableException(HttpMessageNotReadableException e) {
        error();
        log.error("请求参数校验失败:{}", e);
        return ResultHelper.fail(ErrorCode.REQ_PARAM_NOT_IS_JSON,e.getMessage());
    }

    /**
     * handlerMissingServletRequestParameterException
     * 参数缺失
     * @param e -
     * @return java.lang.Object -
     * @author 阳春白雪 | sample@gmail.com
     * @date 2020-06-03 7:57
     * @since 1.0
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Object handlerMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        error();
        MissingServletRequestParameterException parameterException = (MissingServletRequestParameterException) e;
        String parameterName = parameterException.getParameterName();
        List<FieldErrorVo> modelFieldErrors = Arrays.asList(new FieldErrorVo(parameterName,
                String.format("%s类型参数缺失", parameterException.getParameterType()), null));
        log.error("请求参数缺失:{}", JSON.toJSONString(modelFieldErrors));
        return ResultHelper.fail(ErrorCode.REQ_PARAM_ERROR,modelFieldErrors);
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public Object methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        error();
        return ResultHelper.fail(ErrorCode.REQ_PARAM_ERROR,e.getMessage());
    }

    /**
     * methodArgumentNotValidException
     * 参数绑定失败
     * @param e -
     * @return java.lang.Object -
     * @author 阳春白雪 | sample@gmail.com
     * @date 2020-06-03 7:59
     * @since 1.0
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Object methodArgumentNotValidException(BindException e) {
        error();
        List<FieldErrorVo> modelFieldErrors = new ArrayList<FieldErrorVo>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            modelFieldErrors
                    .add(new FieldErrorVo(fieldError.getField(), fieldError.getCode(), fieldError.getRejectedValue()));
        }
        log.error("请求参数校验失败:{}", JSON.toJSONString(modelFieldErrors));
        return ResultHelper.fail(ErrorCode.REQ_PARAM_ERROR,modelFieldErrors);
    }

    @ResponseBody
    @ExceptionHandler(value = FrameworkException.class)
    public Object businessException(FrameworkException e) {
        error();
        log.error("业务处理错误-错误码：【{}】-错误消息：【{}】", e.getCode(), e.getMessage(), e);
        return ResultHelper.fail(e);
    }


    @ResponseBody
    @ExceptionHandler(value = BadSqlGrammarException.class)
    public Object sqlException(BadSqlGrammarException e) {
        error();
        log.error("sql语句错误：【{}】", e.getSql(), e);
        return ResultHelper.fail(ErrorCode.QUERY_DATABASE_ERROR,e.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(value = SQLException.class)
    public Object sqlException(SQLException e) {
        error();
        log.error("sql语句错误：【{}】", e.getSQLState(), e);
        return ResultHelper.fail(ErrorCode.QUERY_DATABASE_ERROR,e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler
    public Object defaultException(Exception e) {
        error();
        log.error("未知错误：【{}】", e.getClass().getName(), e);
        return ResultHelper.fail(ErrorCode.UNDEFINED_ERROR,e.getMessage());
    }

    public void error() {
        if (log.isErrorEnabled()) {
            String q = request.getQueryString();
            String url = request.getRequestURL().toString();
            if (StringUtil.isNotEmpty(q)) {
                try {
                    q = URLDecoder.decode(q, StringPool.DEFAULT_ENCODING);
                } catch (UnsupportedEncodingException e1) {
                    log.warn("参数解码错误");
                }
                url += "?" + q;
            }
            log.error("处理请求【{}】 时出错", url);
        }
    }


}

