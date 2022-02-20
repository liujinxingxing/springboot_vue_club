package com.club.auth.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.club.auth.util.PermissionChecker;
import com.club.base.bean.Result;
import com.club.base.constant.Constants;
import com.club.base.constant.ErrorCode;
import com.club.base.constant.StringPool;
import com.club.base.helper.ResultHelper;
import com.club.framework.cache.CacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AuthFilter的描述 权限拦截器 拦截权限信息
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    @Override
    protected void initFilterBean() throws ServletException {
        // 初始化权限，并加入缓存
        log.debug("初始化权限");
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream("permission/checker.json");
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer buf = new StringBuffer();
            for (String line = ""; (line = reader.readLine()) != null; ) {
                buf.append(line);
            }
            JSONObject json = JSONObject.parseObject(buf.toString());
            for (Map.Entry<String, Object> entry : json.entrySet()) {
                JSONArray value = (JSONArray)entry.getValue();
                List<String> permittes = new ArrayList<>(30);
                for (Object o : value) {
                    permittes.add((String) o);
                }
                CacheManager.put(Constants.AUTH_CACHE_KEY, entry.getKey(), permittes);
            }
        }catch (Exception e){

        }finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(reader);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        int level = (int)request.getAttribute("level");
        String path = request.getRequestURI();
        log.debug("用户等级：{}，访问路径：{}",level,path);
        if(level > 2){
            chain.doFilter(request,response);
            return;
        }
        while (level > -1){
            List<String> permittes = CacheManager.get(Constants.AUTH_CACHE_KEY, Integer.toString(level--), List.class);
            // 登陆用户权限判定
            if (PermissionChecker.isPermitted(permittes, path)) {
                chain.doFilter(request,response);
                return;
            }
        }
        log.warn("无访问权限：{}",path);
        response.setCharacterEncoding(StringPool.DEFAULT_ENCODING);
        response.setHeader(StringPool.CONTENT_TYPE, StringPool.CNT_HTML_UTF8);
        Result<Object> fail = ResultHelper.fail(ErrorCode.NO_PERMISSION_TO_ACCESS);
        fail.setInfo(path);
        response.getWriter().write(JSON.toJSONString(fail));
    }


}
