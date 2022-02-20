package com.club.base.bean;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Result的包装类描述
 * @model 操作结果
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public class Result<T> {

    /**
     * 是否处理成功
     */
    private boolean success;

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应消息
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String message;


    /**
     * 响应数据
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;

    /**
     * 下拉分页专用，
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Boolean next;

    /**
     * 下拉分页专用，
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Long total;

    /**
     * 信息描述，
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Object info;

    public Result() {
        this.success = true;
    }

    public Result(boolean success) {
        this(success, 0);
    }

    public Result(T data) {
        this(true, null, data);
    }

    public Result(boolean success, int code) {
        this(success, code, null);
    }

    public Result(boolean success, int code, T data) {
        this(success, code, null, data);
    }

    public Result(boolean success, int code, String message, T data, Boolean next) {
        this.next = next;
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        if(success){
            this.code = 200;
        }
    }

    public Result(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        if(success){
            this.code = 200;
        }
    }

    public Result(boolean success, ResultMessage errorCode, T data) {
        this.success = success;
        this.data = data;
        if (errorCode != null) {
            this.code = errorCode.getCode();
            this.message = errorCode.getMessage();
        }
        if(success){
            this.code = 200;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return (T) data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getNext() {
        return next;
    }

    public void setNext(Boolean next) {
        this.next = next;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }
}
