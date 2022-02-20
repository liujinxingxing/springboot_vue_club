package com.club.framework.spring.execute;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 批处理，spring 加载完成处理 ，序列处理方法
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecuteMethon {

    /**
     * 执行优先级
     * @ignore
     * @return int|描述
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    int level() default Invoker.MIDDLE;

    /**
     * 是否开启
     * @ignore
     * @return boolean|描述
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    boolean open() default true;


}
