package com.club.core.controller;

import com.club.auth.util.PermissionChecker;
import com.club.base.bean.Page;
import com.club.base.bean.User;
import com.club.base.constant.Constants;
import com.club.base.constant.ErrorCode;
import com.club.base.constant.StringPool;
import com.club.base.helper.ResultHelper;
import com.club.base.util.StringUtil;
import com.club.core.code.ValidateCode;
import com.club.core.model.AuthUser;
import com.club.core.service.UserService;
import com.club.core.vo.PermissionVo;
import com.club.framework.cache.CacheManager;
import com.club.framework.mybatis.helper.GridResultHelper;
import com.club.framework.spring.controller.BaseController;
import com.club.framework.valid.groups.Modify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 基本API ,游客能访问的全部接口
 * @action 基本分组
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
@RestController
@RequestMapping("auth")
public class AuthController extends BaseController {

    @Autowired
    protected UserService userService;

    /**
     * 登录系统
     *
     * @param user  |com.club.core.model.AuthUser|用户
     * @param code  |String|验证码
     * @param realm |String|必须|normal|领域   admin（超管） vip(会员)   normal(普通)
     * @return com.club.base.bean.User|登录用户信息
     * @summary 登录系统
     * @error 账号/密码错误
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @PostMapping("{realm}/{code}/signIn")
    public Object signIn(@RequestBody @Validated(Modify.class) AuthUser user, @PathVariable String realm, @PathVariable String code) {
        String s = CacheManager.get("CODE_CACHE_KEY", "signIn", String.class);
        if (StringUtil.equalsIgnoreCase(code, s)) {
            AuthUser auth = userService.selectByPrimaryKey(user.getAccount());
            if (auth == null) {
                return ResultHelper.fail(ErrorCode.ACCOUNT_INCORRECT);
            }
            // 判断领域
            int leveles = 1;
            if ("admin".equals(realm)) {
                leveles = 3;
            } else if ("vip".equals(realm)) {
                leveles = 2;
            }
            if (leveles > auth.getLeveles()) {
                return ResultHelper.fail(ErrorCode.ACCOUNT_ACCESS);
            }
            if (StringUtil.equals(StringUtil.getSHA256StrJava(user.getPassword()), auth.getPassword())) {
                User u = new User();
                String uuid = UUID.randomUUID().toString();
                response.setHeader("Set-Cookie", String.format("%s=%s;Path=/;HttpOnly", userService.getToken(), uuid));
                u.setSessionId(uuid);
                u.setRealm(realm);
                u.setUserName(auth.getUserName());
                u.setEmail(auth.getEmail());
                u.setAccount(auth.getAccount());
                u.setAvatar(auth.getAvatar());
                u.setGender(auth.getGender());
                u.setDescripter(auth.getDescripter());
                u.setLevel(leveles);
                u.setCreateTime(auth.getCreateTime() != null ?auth.getCreateTime().getTime():0);
                CacheManager.put(userService.getToken(), uuid, u, userService.getTimeout(), TimeUnit.MINUTES);
                userService.login(uuid,auth.getAccount());
                return ResultHelper.success(u);
            }
            return ResultHelper.fail(ErrorCode.PASSWORD_INCORRECT);
        }
        return ResultHelper.fail(ErrorCode.VALID_CODE_INCORRECT);
    }

    /**
     * 退出登录 id 不为空时，超管清除其他人的会话, id 不为空只在管理员权限有效
     *
     * @param id  |String|会话id
     * @return void|退出登录
     * @summary 退出登录
     * @error 错误描述
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("signOut")
    public Object signOut(String id) {
        userService.signOut(id,getUser());
        return ResultHelper.success();
    }

    /**
     * 获取用户登录日志信息列表
     * @param keyword |string|关键字搜索
     * @param page |int|非必须|1|分页当前页
     * @param rows |int|非必须|10|分页行数
     * @param roll |boolean|非必须|false|是否滚动分页
     * @param enable |boolean|非必须|true|是否分页
     * @summary 录日志列表
     * @return com.club.core.model.LoginLog|登录日志
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping("list")
    public Object list(String keyword,Page page){
        return GridResultHelper.grid(userService.listLog(keyword,page, getUser()),page);
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return com.club.base.bean.User|用户信息
     * @summary 获取当前用户
     * @error 错误描述
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("getCurrentUser")
    public Object getCurrentUser() {
        log.debug("获取当前用户");
        return ResultHelper.success(getUser());
    }

    /**
     * 获取验证码
     *
     * @param width  |int|非必须|100|图片长度
     * @param height |int|非必须|34|图片高度
     * @param valid  |String|验证码作用域
     * @return void|图片流
     * @summary 获取验证码
     * @error 错误描述
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("{valid}/code")
    public void code(@RequestParam(value = "width", defaultValue = "100") int width, @RequestParam(value = "height", defaultValue = "34") int height, @PathVariable String valid) {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            ValidateCode code = new ValidateCode(width, height, 4, 100);
            CacheManager.put("CODE_CACHE_KEY", valid, code.getCode(), 2, TimeUnit.MINUTES);
            code.write(response.getOutputStream());
        } catch (Exception e) {
        }
    }

    /**
     * 获取权限信息，是否有权限
     *
     * @param module      |String|module，模块名称
     * @param permissions |String|权限值
     * @return java.lang.Object|权限值：true/false
     * @summary 获取权限信息
     * @error 错误描述
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("hasPermissions")
    public Object hasPermissions(String module, @RequestParam(value = "permissions", required = true) List<String> permissions) {
        return ResultHelper.success(hasPermissions(module, permissions, getUser()));
    }

    /**
     * 获取权限信息，是否有权限
     *
     * @param vo |com.club.core.vo.PermissionVo|权限
     * @return java.lang.Object|权限值：true/false
     * @summary 获取权限信息
     * @error 错误描述
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @PostMapping(value = "hasPermission")
    public Object hasPermissions(@RequestBody PermissionVo vo) {
        return ResultHelper.success(hasPermissions(vo.getModule(), vo.getPermissions(), getUser()));
    }

    protected Map<String, Boolean> hasPermissions(String module, List<String> permissions, User user) {
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        if (user.isAdmin()) {
            for (String permissionName : permissions) {
                result.put(permissionName, true);
            }
            return result;
        }
        String func = "func:";
        int level = user.getLevel();
        // 获取真实的模块
        module = getRealModule(module);
        List<String> permittes = new ArrayList<>(100);
        while (level > -1) {
            List list = CacheManager.get(Constants.AUTH_CACHE_KEY, Integer.toString(level--), List.class);
            // 登陆用户权限判定
            if (list != null) {
                permittes.addAll(list);
            }
        }
        for (String permissionName : permissions) {
            String permission = permissionName;
            if (StringUtil.isNotEmpty(module)) {
                permission = String.format("%s:%s", module, permissionName);
            }
            // 判断权限
            boolean permitted = PermissionChecker.hasPermitted(permittes, func.concat(permission));
            log.trace("permitted：{}", permitted);
            log.trace("权限值：{}", func.concat(permission));
            result.put(permissionName, permitted);
        }
        return result;
    }

    protected String getRealModule(String module) {
        if (StringUtil.isNotEmpty(module)) {
            module = module.replace(StringPool.PATHSPLIT, StringPool.PERMISSIONSPLIT);
            if (module.startsWith(StringPool.PERMISSIONSPLIT)) {
                module = module.substring(1);
            }
            if (module.endsWith(StringPool.PERMISSIONSPLIT)) {
                module = module.substring(0, module.length() - 1);
            }
        }
        return module;
    }
}
