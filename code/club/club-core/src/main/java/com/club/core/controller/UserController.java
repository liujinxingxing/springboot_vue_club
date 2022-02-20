package com.club.core.controller;

import com.club.base.bean.Page;
import com.club.base.bean.User;
import com.club.base.constant.ErrorCode;
import com.club.base.helper.ResultHelper;
import com.club.core.model.AuthUser;
import com.club.core.service.UserService;
import com.club.framework.cache.CacheManager;
import com.club.framework.mybatis.helper.GridResultHelper;
import com.club.framework.spring.controller.BaseController;
import com.club.framework.valid.groups.Insert;
import com.club.framework.valid.groups.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户处理接口 处理用户的所有接口
 * @action 用户分组
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    protected UserService userService;

    /**
     * 保存用户信息-管理者保存（ 新增或保存 ）
     * @param user |com.club.core.model.AuthUser|用户
     * @param type |String|更新类型  update 更新 ，insert 新建
     * @summary 保存用户信息
     * @error 没有更新
     * @return com.club.core.model.AuthUser|用户
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @PostMapping("{type}/save")
    public Object save(@RequestBody @Validated(Insert.class) AuthUser user,@PathVariable String type) {
        if ("insert".equals(type)) {
            return ResultHelper.success(userService.insertUser(user, getUser()));
        }
        if ("update".equals(type)) {
            return ResultHelper.success(userService.updateUser(user, getUser()));
        }
        return ResultHelper.fail(ErrorCode.NO_FOUND_SERVICE);
    }

    /**
     * 保存用户信息-用户自己保存
     * @param user |com.club.core.model.AuthUser|用户
     * @summary 保存用户信息
     * @error 没有更新
     * @return int|更新条数
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @PostMapping("modify")
    public Object modify(@RequestBody @Validated(Update.class) AuthUser user) {
        User u = getUser();
        AuthUser auth = userService.modify(user, u);
        u.setUserName(auth.getUserName());
        u.setEmail(auth.getEmail());
        u.setAccount(auth.getAccount());
        u.setAvatar(auth.getAvatar());
        u.setGender(auth.getGender());
        u.setDescripter(auth.getDescripter());
        CacheManager.put(userService.getToken(), u.getSessionId(), u, userService.getTimeout(), TimeUnit.MINUTES);
        return ResultHelper.success(u);
    }

    /**
     * 修改用户密码-用户自己保存
     * @param old |String|用户旧密码
     * @param password |String|用户新密码
     * @summary 保存用户密码
     * @error 没有更新
     * @return int|更新条数
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @PostMapping("passwd")
    public Object passwd(@RequestParam String old,@RequestParam String password) {
        return ResultHelper.success(userService.passwd(old, password, getUser()));
    }

    /**
     * 用户账号是否存在 ，监测账号是否重复
     * @param account |String|用户账号
     * @summary 用户账号是否存在
     * @error 没有更新
     * @return boolean|更新条数
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @PostMapping("exsist")
    public Object exsist(@RequestParam String account) {
        return ResultHelper.success(userService.exsist(account));
    }

    /**
     * 获取用户信息列表
     * @param keyword |String|关键搜索词
     * @param page |int|非必须|1|分页当前页
     * @param rows |int|非必须|10|分页行数
     * @param roll |boolean|非必须|false|是否滚动分页
     * @param enable |boolean|非必须|true|是否分页
     * @summary 获取公告信息列表
     * @return com.club.core.model.AuthUser[]|用户信息
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping("list")
    public Object list(String keyword, Page page){
        return GridResultHelper.grid(userService.list(keyword, page, getUser()),page);
    }

    /**
     * 删除用户登录日志
     * @param ids |String|日志ids
     * @summary 删除用户登录日志
     * @return int|更新条数
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping(value = "log/delete")
    public Object remove(@RequestParam(value = "ids") List<String> ids){
        return ResultHelper.success(userService.removeLog(ids, getUser()));
    }

    /**
     * 删除用户信息
     * @param ids |String|用户ids
     * @summary 删除用户信息
     * @return int|更新条数
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping(value = "delete")
    public Object delete(@RequestParam(value = "ids") List<String> ids){
        return ResultHelper.success(userService.delete(ids, getUser()));
    }

    /**
     * 导出用户
     * @param ids |String|用户id，无用户id,代表获取全部
     * @param keyword |String|关键搜索字
     * @summary 导出用户
     * @error 错误描述
     * @return void|用户信息,excel文件流
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping("export")
    public Object exportUser(@RequestParam(value = "ids",required = false) List<String> ids,String keyword) {
        if(userService.exportUser(request, response, ids, keyword, getUser())){
            return null;
        }
        return ResultHelper.fail(ErrorCode.NO_DATA);
    }

    /**
     * 重置用户密码  初始密码为123456
     * @param ids |String|用户ids
     * @summary 重置密码
     * @error 错误描述
     * @return int|修改数量
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping("init")
    public Object init(@RequestParam(value = "ids",required = false) List<String> ids) {
        return ResultHelper.success(userService.init(ids, getUser()));
    }

    /**
     * 导入用户数据
     * @param users |com.club.core.model.AuthUser[]|用户信息
     * @exclude user.createTime,user.password
     * @summary 导入用户数据
     * @error 错误描述
     * @return int|导入用户数量
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping(value = "import" ,method = RequestMethod.POST)
    public Object importUser(@RequestBody List<AuthUser> users){
        return ResultHelper.success(userService.saveListUser(users, getUser()));
    }
}
