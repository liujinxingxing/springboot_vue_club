package com.club.framework.spring.service;

import com.club.framework.spring.execute.ExecuteMethon;
import com.club.framework.spring.execute.Invoker;
import com.club.framework.spring.execute.InvokerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * spring 后置处理
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Slf4j
@Component
public class BeanPostProcessorImpl implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        Class<? extends Object> clazz = bean.getClass();
        Method[] methods = clazz.getMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                Annotation[] annotations = method.getAnnotations();
                for (Annotation anno : annotations) {
                    if(anno instanceof ExecuteMethon){
                        ExecuteMethon methon = (ExecuteMethon) anno;
                        if(methon.open()){
                            log.debug("正在加载类{}中的{}方法",s, method.getName());
                            InvokerHelper.addExecute(Invoker.valueOf(method, bean, methon.level()));
                        }
                    }
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        //通过反射获取类
        log.debug("正在加载类{}",bean.getClass().getName());
        return bean;
    }
}
