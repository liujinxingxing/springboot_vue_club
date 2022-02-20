package com.club.framework.spring.spring;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;


/**
 * BeanNameGenerator的描述
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class BeanNameGenerator extends AnnotationBeanNameGenerator {

	@Override
	protected String buildDefaultBeanName(BeanDefinition definition) {
		String className = definition.getBeanClassName();
		className = className.substring(0, 1).toLowerCase() + className.substring(1);
		LoggerFactory.getLogger(getClass()).trace("init bean {} id:{}", definition.getBeanClassName(), className);
		return className;
	}

}
