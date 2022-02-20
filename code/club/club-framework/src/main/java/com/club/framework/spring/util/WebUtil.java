package com.club.framework.spring.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * WebUtil的描述
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Slf4j
public class WebUtil {

    protected static final ThreadLocal<Integer> TIMEOUT_CONNECT = new ThreadLocal<Integer>();
    protected static final ThreadLocal<Integer> TIMEOUT_READ = new ThreadLocal<Integer>();
    protected static final int DEFAULT_TIMEOUT_CONNECT = 30 * 1000;
    protected static final int DEFAULT_TIMEOUT_READ = 30 * 1000;

    protected static String enc = null;


    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-real-host-ip");
        if (StringUtils.isEmpty(ip)) {
            ip = request.getRemoteHost();
        }
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.warn("访问地址无法获取:{}", e.getMessage());
            }
        }
        return ip;
    }

    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (StringUtils.isEmpty(userAgent)) {
            return null;
        }
        return userAgent;
    }


    /**
     * 设置连接超时
     * @param sec - 超时时间,秒，0时不限制超时
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static void setConnTime(int sec) {
        TIMEOUT_CONNECT.set(sec * 1000);
    }

    /**
     * 设置读取超时
     * @param sec - 超时时间,秒，0时不限制超时
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static void setReadTime(int sec) {
        TIMEOUT_READ.set(sec * 1000);
    }

    /**
     * 获取连接时间
     * @return int - 连接时间
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    protected static int getConnTime() {
        Integer t = TIMEOUT_CONNECT.get();
        TIMEOUT_CONNECT.remove();
        return t == null ? DEFAULT_TIMEOUT_CONNECT : t;
    }

    /**
     * 获取读取时间
     * @return int - 读取时间
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    protected static int getReadTime() {
        Integer t = TIMEOUT_READ.get();
        TIMEOUT_READ.remove();
        return t == null ? DEFAULT_TIMEOUT_READ : t;
    }

}
