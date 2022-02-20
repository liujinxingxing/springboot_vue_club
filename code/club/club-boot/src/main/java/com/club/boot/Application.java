package com.club.boot;

import com.club.framework.spring.spring.BeanNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Application的描述
 * @ignore
 * @author 阳春白雪 | sample@gmail.com
 * @date 2022年2月20日
 * @since 1.0
 */
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = "com.club", nameGenerator = BeanNameGenerator.class)
public class Application extends SpringBootServletInitializer {

    /**
     * 入口函数
     * @param args |参数类型|必须|默认值|参数类型 body/form/path|参数描述
     * @error 错误描述
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    /**
     * servlet
     * @param application |参数类型|必须|默认值|参数类型 body/form/path|参数描述
     * @error 错误描述
     * @return org.springframework.boot.builder.SpringApplicationBuilder|描述
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
