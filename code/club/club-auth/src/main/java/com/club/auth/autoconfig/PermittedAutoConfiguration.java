package com.club.auth.autoconfig;

import com.club.auth.filter.AuthFilter;
import com.club.auth.filter.UserFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PermittedAutoConfiguration的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
@Configuration
public class PermittedAutoConfiguration {

    @Value("${base.session.name:token}")
    private String token;
    @Value("${base.session.timeout:20}")
    private int timeout;

    @Bean
    public UserFilter userFilter() {
        return new UserFilter(token, timeout);
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public DelegatingFilterProxyRegistrationBean userRequestFilterBean() {
        DelegatingFilterProxyRegistrationBean filter = new DelegatingFilterProxyRegistrationBean("userFilter");
        filter.setAsyncSupported(true);
        filter.addInitParameter("targetFilterLifecycle", "true");
        filter.setEnabled(true);
        filter.addUrlPatterns("/*");
        filter.setOrder(1);
        return filter;
    }

    @Bean
    public DelegatingFilterProxyRegistrationBean authRequestFilterBean() {
        DelegatingFilterProxyRegistrationBean filter = new DelegatingFilterProxyRegistrationBean("authFilter");
        filter.setAsyncSupported(true);
        filter.addInitParameter("targetFilterLifecycle", "true");
        filter.setEnabled(true);
        filter.addUrlPatterns("/*");
        filter.setOrder(3);
        return filter;
    }
}
