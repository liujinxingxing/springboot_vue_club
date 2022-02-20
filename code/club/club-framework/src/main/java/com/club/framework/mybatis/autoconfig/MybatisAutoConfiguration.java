package com.club.framework.mybatis.autoconfig;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.club.framework.spring.spring.BeanNameGenerator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * @author 阳春白雪 | sample@gmail.com
 */
@Configuration
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
@ImportResource("classpath:spring/mybatis-context.xml")
public class MybatisAutoConfiguration {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer bean = new MapperScannerConfigurer();
        bean.setBasePackage("com.**.mapper");
        bean.setNameGenerator(new BeanNameGenerator());
        Properties p = new Properties();
        p.setProperty("ORDER","BEFORE");
        bean.setProperties(p);
        return bean;
    }


}
