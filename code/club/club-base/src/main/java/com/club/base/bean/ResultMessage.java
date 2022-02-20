package com.club.base.bean;

/**
 * 结果消息接口，用于定义全局消息接口
 * @model 结果消息接口
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public interface ResultMessage {

    /**
     * 错误代码
     */
    int getCode();

    /**
     * 错误信息
     */
    String getMessage();


}
