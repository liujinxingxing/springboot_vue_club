/**
 * @BelongsProject： base
 * @BelongsPackage： com.daysh.base.util
 * @author 阳春白雪 | sample@gmail.com
 * @CreateTime： 2020-01-30 17:26
 *
 */
package com.club.auth.util;

import com.club.base.constant.StringPool;
import com.club.base.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限判断工具
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class PermissionChecker {

    /**
     * 功能描述: 判断权限
     * @param permission
     * @param url
     * @return boolean - 返回值类型
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     */
    public static boolean isPermitted(String permission,String url){
        if(StringUtil.equals(StringPool.START,permission)){
            return true;
        }
        int index = StringUtil.indexOf(url, StringPool.QUERY);
        if(index != -1){
            url = StringUtil.substring(url, 0, index);
        }
        String[] split = StringUtil.split(permission, StringPool.PERMISSIONSPLIT);
        String[] urlSplit = StringUtil.split(url, StringPool.PATHSPLIT);
        if((split.length-1) > urlSplit.length){
            return false;
        }
        if(!split[0].equalsIgnoreCase(StringPool.URL_PREFIX)){
            return false;
        }
        for (int i = 1; i < split.length; i++) {
            if(StringPool.START.equals(split[i])){
                continue;
            }
            String[] sina = StringUtil.split(split[i], StringPool.COMMA);
            boolean exsis = false;
            for (String single : sina) {
                if(StringUtil.equals(single, urlSplit[i-1])){
                    exsis = true;
                    break;
                }
            }
            if(!exsis){
                return false;
            }
        }
        return true;
    }

    /**
     * 功能描述: 判断权限
     * @param permissions
     * @param url
     * @return boolean - 返回值类型
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     */
    public static boolean isPermitted(List<String> permissions,String url){
        if(CollectionUtils.isEmpty(permissions)){
            return false;
        }
        for (String permission : permissions) {
            if(isPermitted(permission, url)){
                return true;
            }
        }
        return false;
    }

    /**
     * 功能描述: 判断常规权限
     * @param permission
     * @param permissionUrl
     * @return boolean - 返回值类型
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     */
    public static boolean hasPermitted(String permission,String permissionUrl){
        String[] split = StringUtil.split(permission, StringPool.PERMISSIONSPLIT);
        String[] urlSplit = StringUtil.split(permissionUrl, StringPool.PERMISSIONSPLIT);
        if((split.length-1) > urlSplit.length){
            return false;
        }
        for (int i = 0; i < split.length; i++) {
            if(StringPool.START.equals(split[i])){
                continue;
            }
            String[] sina = StringUtil.split(split[i], StringPool.COMMA);
            boolean exsis = false;
            for (String single : sina) {
                if(StringUtil.equals(single, urlSplit[i])){
                    exsis = true;
                    break;
                }
            }
            if(!exsis){
                return false;
            }
        }
        return true;
    }

    /**
     * 功能描述: 判断常规权限
     * @param permissions
     * @param permissionUrl
     * @return boolean - 返回值类型
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     */
    public static boolean hasPermitted(List<String> permissions,String permissionUrl){
        if(CollectionUtils.isEmpty(permissions)){
            return false;
        }
        for (String permission : permissions) {
            if(hasPermitted(permission, permissionUrl)){
                return true;
            }
        }
        return false;
    }

    /**
     * 功能描述: 判断常规权限
     * @param permissions
     * @param permissionUrls
     * @return Map<String,Boolean> - 返回值类型
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     */
    public static Map<String, Boolean> hasPermitted(List<String> permissions,List<String> permissionUrls){
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        for (String permissionUrl : permissionUrls) {
            result.put(permissionUrl, hasPermitted(permissions, permissionUrl));
        }
        return result;
    }

    public static String getRealPath(String path){
        String p = path;
        p = p.replaceAll(StringPool.PATHSPLIT, StringPool.PERMISSIONSPLIT);
        if (!p.startsWith(StringPool.PERMISSIONSPLIT)) {
            p = StringPool.PERMISSIONSPLIT.concat(p);
        }
        p = StringPool.URL_PREFIX.concat(p);
        return p;
    }

}
