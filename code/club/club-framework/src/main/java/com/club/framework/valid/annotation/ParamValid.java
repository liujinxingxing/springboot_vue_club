package com.club.framework.valid.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.club.framework.valid.Regulation;

/**
 * 参数校验注解
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamValid {

    /**
     * 正则表达式
     * @return java.lang.String -正则表达式
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    String regex() default Regulation.SPECIAL_CHARACTERS_REGEXP;

    /**
     * 消息
     * @return java.lang.String - 消息
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    String message() default "校验规则:1-30位大小写字母数字下划线中文";

}