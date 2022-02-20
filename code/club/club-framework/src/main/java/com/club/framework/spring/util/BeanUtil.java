package com.club.framework.spring.util;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * BeanUtil的描述
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Component
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext ctx;

    public static String getValue(String key,String def){
        String property = ctx.getEnvironment().getProperty(key);
        if(StringUtils.isNotEmpty(property)){
            return property;
        }
        if(StringUtils.isNotEmpty(def)){
            return def;
        }
        return null;
    }

    public static String getValue(String key){
        return getValue(key,null);
    }

    public static int getInteger(String key){
        return getInteger(key,0);
    }

    public static int getInteger(String key,int def){
        String value = getValue(key, null);
        if(StringUtils.isNotEmpty(value)){
            return Integer.parseInt(value);
        }
        return def;
    }

    public static Object getBean(String name) {
        return ctx.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return ctx.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return ctx.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) {
        return ctx.getBean(requiredType, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtil.ctx = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    /**
     * 动态的向spring上下文注入bean
     */
    public static <T> void registBean(String beanName, BeanDefinitionBuilder beanDefinitionBuilder) {
        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
        dbf.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * 卸载bean
     */
    public static void destoryBean(String beanName) {
        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
        dbf.removeBeanDefinition(beanName);
    }

    public static DefaultListableBeanFactory defaultListableBeanFactory() {
        return  (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
    }
}

