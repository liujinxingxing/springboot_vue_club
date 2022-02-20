package com.club.core.service;

import com.club.base.bean.CacheTrigger;
import com.club.base.bean.Page;
import com.club.base.bean.User;
import com.club.base.constant.ErrorCode;
import com.club.base.exception.FrameworkException;
import com.club.base.util.StringUtil;
import com.club.core.mapper.LoginLogMapper;
import com.club.core.mapper.UserMapper;
import com.club.core.model.AuthUser;
import com.club.core.model.LoginLog;
import com.club.core.util.ExcelUtil;
import com.club.framework.cache.CacheManager;
import com.club.framework.mybatis.helper.PageHelper;
import com.club.framework.mybatis.service.AbstractService;
import com.club.framework.spring.execute.ExecuteMethon;
import com.club.framework.spring.execute.Invoker;
import com.club.framework.valid.groups.Insert;
import com.club.framework.valid.util.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * UserService的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
@Slf4j
@Service
public class UserService extends AbstractService<AuthUser> implements CacheTrigger {

    @Value("${base.session.name:token}")
    private String token;
    @Value("${base.session.timeout:20}")
    private int timeout;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected LoginLogMapper loginLogMapper;

    public AuthUser insertUser(AuthUser u, User user) {
        u.setCreateTime(new Date());
        u.setPassword(getPassword());
        if(insertSelective(u, user) > 0){
            return u;
        }
        throw new FrameworkException(ErrorCode.NO_UPDATE);
    }

    public AuthUser updateUser(AuthUser u, User user) {
        if(updateByPrimaryKeySelective(u,user)> 0){
            return u;
        }
        throw new FrameworkException(ErrorCode.NO_DATA);
    }

    public boolean exsist(String account){
        Example example = new Example(AuthUser.class);
        example.createCriteria().andEqualTo(AuthUser.FIELD_ACCOUNT,account);
        return userMapper.selectCountByExample(example) > 0;
    }

    public AuthUser modify(AuthUser auth,User user){
        AuthUser u = selectByPrimaryKey(user.getAccount());
        if(u != null){
            u.setUserName(auth.getUserName());
            u.setEmail(auth.getEmail());
            u.setGender(auth.getGender());
            u.setAvatar(auth.getAvatar());
            u.setUpdateTime(new Date());
            u.setDescripter(auth.getDescripter());
            if(updateByPrimaryKeySelective(u) > 0){
                return u;
            }
            throw new FrameworkException(ErrorCode.NO_UPDATE);
        }
        throw new FrameworkException(ErrorCode.NO_DATA);
    }

    @Transactional
    public int saveListUser(List<AuthUser> users, User user) {
        int ret = 0;
        for (AuthUser u : users) {
            try {
                ValidateUtils.validate(u, Insert.class);
                if(exsist(u.getAccount())){
                    continue;
                }
                int leveles = u.getLeveles();
                if(leveles < 1 || leveles > 3){
                    u.setLeveles(1);
                }
                u.setAvatar(null);
                u.setPassword(getPassword());
                u.setCreateTime(new Date());
                ret += insertSelective(u, user);
            }catch (Exception e) {
            }
        }
        return ret;
    }

    private String getPassword() {
        return "49dc52e6bf2abe5ef6e2bb5b0f1ee2d765b922ae6cc8b95d39dc06c21c848f8c";
    }

    public boolean exportUser(HttpServletRequest req, HttpServletResponse resp, List<String> userIds, String keyword, User user) {
        List<AuthUser> result = userMapper.list(StringUtil.keyword(keyword), userIds);
        if (CollectionUtils.isNotEmpty(result)) {
            String[] title = {"电话", "名称", "性别", "类型", "邮箱"};
            String[][] content = new String[result.size()][title.length];
            int count = 0;
            for (AuthUser vo : result) {
                int num = 0;
                content[count][num++] = vo.getAccount();
                content[count][num++] = vo.getUserName();
                content[count][num++] = getGender(vo.getGender());
                content[count][num++] = getLeveles(vo.getLeveles());
                content[count][num++] = vo.getEmail();
                count++;
            }
            return ExcelUtil.exportData(req, resp, "导出用户", title, content);
        }
        return false;
    }
    private String getLeveles(int gender) {
        switch (gender){
            case 3:return "管理员";
            case 2:return "VIP";
            default:return "普通";
        }
    }
    private String getGender(int gender) {
        switch (gender){
            case 0:return "保密";
            case 1:return "男";
            case 2:return "女";
            default:return "其他";
        }
    }

    public int passwd(String old,String password, User user){
        AuthUser u = selectByPrimaryKey(user.getAccount());
        if(u != null){
            old = StringUtil.getSHA256StrJava(old);
            if(StringUtil.equals(u.getPassword(),old)){
                password = StringUtil.getSHA256StrJava(password);
                u.setPassword(password);
                u.setUpdateTime(new Date());
                return updateByPrimaryKeySelective(u);
            }
            throw new FrameworkException(ErrorCode.PASSWORD_INCORRECT);
        }
        throw new FrameworkException(ErrorCode.ACCOUNT_INCORRECT);
    }

    public List<AuthUser> list(String keyword, Page page, User user) {
        PageHelper.startPage(page);
        return userMapper.list(StringUtil.keyword(keyword),null);
    }

    @Transactional
    public int init(List<String> ids, User user) {
        return userMapper.init(getPassword(),ids);
    }

    @Transactional
    public int delete(List<String> ids, User user) {
        int ret = 0;
        if(CollectionUtils.isNotEmpty(ids)){
            Example example = new Example(AuthUser.class);
            example.createCriteria().andIn(AuthUser.FIELD_ACCOUNT,ids).andNotEqualTo(AuthUser.FIELD_ACCOUNT,user.getAccount());
            ret = deleteByExample(example);
        }
        return ret;
    }

    @Transactional
    public int removeLog(List<String> ids, User user){
        int ret = 0;
        if(CollectionUtils.isNotEmpty(ids)){
            for (String id : ids) {
                Example example = new Example(LoginLog.class);
                example.createCriteria().andEqualTo(LoginLog.FIELD_UUID,id);
                if(loginLogMapper.deleteByExample(example) > 0){
                    ret++;
                    signOut(id,user);
                }
            }
        }
        return ret;
    }

    public List<LoginLog> listLog(String keyword,Page page, User user){
        PageHelper.startPage(page);
        return userMapper.listLog(StringUtil.keyword(keyword), user.isAdmin(), user.getAccount());
    }

    public void login(String uuid,String account){
        loginLogMapper.insertSelective(new LoginLog(uuid,account));
    }

    public void signOut(String sessionId,User user){
        // 清除用户登录信息 user
        if(StringUtil.isNotEmpty(sessionId) && user.isAdmin()){
            CacheManager.evict(getToken(), sessionId);
            logout(sessionId,true);
        }
        if(StringUtil.isEmpty(sessionId)){
            sessionId = user.getSessionId();
            CacheManager.evict(getToken(), sessionId);
            logout(sessionId,true);
        }
    }

    public void logout(String uuid,boolean quit){
        loginLogMapper.updateByPrimaryKeySelective(new LoginLog(uuid,quit));
    }

    @ExecuteMethon(level = Invoker.FIRSTLY)
    public void init() {
        log.debug("设置清除缓存触发器");
        CacheManager.setTrigger(this);
    }

    @Override
    public void triggerKey(String key) {
    }

    @Override
    public void triggerCache(String key, String cache) {
        if(StringUtil.equals(key,getToken())){
            this.logout(cache,false);
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
