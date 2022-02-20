package com.club.auth.filter;

import com.club.base.bean.User;
import com.club.base.constant.StringPool;
import com.club.framework.cache.CacheManager;
import com.club.framework.spring.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * AuthFilter的描述 用户拦截器 拦截用户信息
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
@Slf4j
public class UserFilter extends OncePerRequestFilter {

    private static String TOKEN = "token";
    private static int EXPIRE = 30;

    public UserFilter(String token,int expire) {
        TOKEN = token;
        EXPIRE = expire;
    }

    public UserFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 获取sessionid
        Cookie[] cookies = request.getCookies();
        String session = null;
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(TOKEN.equals(cookie.getName())){
                    session = cookie.getValue();
                    break;
                }
            }
        }
        // 根据sessionid从自定义缓存中获取用户
        User user = CacheManager.get(TOKEN, session, User.class);
        if(user != null){
            // 重新设置超时时间
            CacheManager.put(TOKEN, session, user, EXPIRE, TimeUnit.MINUTES);
        }else {
            user = UserUtil.guest();
            user.setLevel(0);
        }
        // 将用户放入请求域
        request.setAttribute(StringPool.REQUEST_USER,user);
        // 权限等级
        request.setAttribute("level",user.getLevel());
        chain.doFilter(request,response);
    }


}
