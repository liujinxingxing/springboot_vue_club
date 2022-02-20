/**
 *
 *
 * @author 阳春白雪 | sample@gmail.com
 * @CreateTime： 2020-03-09 16:02
 *
 */
package com.club.core.vo;

import java.util.List;

/**
 * 权限的包装类描述
 * @model permission包装类
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public class PermissionVo {

    /**
     * 模块
     */
    private String module;
    /**
     * 权限值
     */
    private List<String> permissions;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
