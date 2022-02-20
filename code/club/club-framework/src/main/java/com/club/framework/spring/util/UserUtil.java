package com.club.framework.spring.util;

import com.alibaba.fastjson.JSONObject;
import com.club.base.bean.User;
import com.club.base.constant.ErrorCode;
import com.club.base.constant.StringPool;
import com.club.base.exception.FrameworkException;
import com.club.base.util.AESUtil;
import com.club.base.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * 用户工具类
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class UserUtil {

    protected static Logger log = LoggerFactory.getLogger(UserUtil.class);

    /**
     * 获取token当前session用户,只能在Controller中使用
     * @param req - 请求
     * @return com.daysh.base.bean.User - 登录用户
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static User getCurrentUser(HttpServletRequest req, String key) throws Exception {
        if (req != null) {
            String userStr = req.getHeader(StringPool.TOKEN_USER);
            if (StringUtil.isNotEmpty(userStr)) {
                // 转换编码,以及解码，和反序化
                log.trace("转换编码,以及解码，和反序化");
                return JSONObject.parseObject(AESUtil.aesDecrypt(URLDecoder.decode(new String(userStr.getBytes(StringPool.GBK_ENCODING), StringPool.DEFAULT_ENCODING), StringPool.DEFAULT_ENCODING), key), User.class);
            }
        }
        throw new FrameworkException(ErrorCode.NO_PERMISSION_TO_ACCESS);
    }

    /**
     * 获取request中的用户,只能在Controller中使用
     * @param req - 请求
     * @return com.daysh.base.bean.User - 登录用户
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static User getRequestUser(HttpServletRequest req) {
        User user = null;
        if (req != null) {
            user = (User) req.getAttribute(StringPool.REQUEST_USER);
        }
        if (req == null || user == null) {
            user = guest();
            user.setSessionId(req.getSession().getId());
        }
        return user;
    }

    /**
     * 2020-02-01  这里是否要进行数据加密，防止被渗透
     * 判断是否存在token
     * @param req - 请求
     * @return boolean - 是否存在token
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static boolean existsTokenUser(HttpServletRequest req) {
        if (req == null) {
            return false;
        }
        String token = req.getHeader(StringPool.TOKEN_USER);
        log.trace("token:{}", token);
        return StringUtil.isNotEmpty(token);
    }

    /**
     * 设置用户到请求作用域
     * @param req  - 请求
     * @param user - 用户
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static void setUser(HttpServletRequest req, User user) {
        if(user != null && req.getAttribute(StringPool.REQUEST_USER) == null){
            req.setAttribute(StringPool.REQUEST_USER, user);
        }
    }

    public static User guest() {
        User user = new User();
        user.setAccount("0000");
        user.setUserName("访客");
        user.setLevel(0);
        return user;
    }

}
