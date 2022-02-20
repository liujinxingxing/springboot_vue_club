package com.club.framework.spring.autoconfig;


import com.club.framework.spring.util.DateUtil;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.util.Date;

/**
 * 配置mvc
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Configuration
@AutoConfigureAfter({DispatcherServletAutoConfiguration.class, org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.class, TaskExecutionAutoConfiguration.class, ValidationAutoConfiguration.class})
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    private static final String[] PATTERNS = new String[]{"y-M-d", "yMd", "y/M/d", "y-M-d H:m:s", "yMd H:m:s", "y/M/d H:m:s", "y-M-d H:m:s.S", "yMd H:m:s.S", "y/M/d H:m:s.S", "y-M-d'T'H:m:s.S",
            "y-M-d'T'H:m:s.S'Z'"};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations(String.format("file:%s/static/",System.getProperty("user.dir")));;
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                try {
                    if (source == null || "".equals(source.trim())) {
                        return null;
                    }
                    if (source.matches("\\d+$")) {
                        long t = Long.parseLong(source);
                        if (t > 0) {
                            return new Date(t);
                        } else {
                            return null;
                        }
                    }
                    return DateUtil.parseDate(source, PATTERNS);
                } catch (ParseException e) {
                    LoggerFactory.getLogger(getClass()).error("convert date error", e);
                }
                return null;
            }
        });
        WebMvcConfigurer.super.addFormatters(registry);
    }

}
