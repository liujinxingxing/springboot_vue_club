package com.club.core.model;

import com.club.base.bean.BaseUser;
import com.club.base.model.BaseModel;
import com.club.core.constant.TableLib;
import com.club.framework.valid.groups.Insert;
import com.club.framework.valid.groups.Modify;
import com.club.framework.valid.groups.Update;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * AuthUser的包装类描述
 * @model 用户信息
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
@Table(name = AuthUser.TABLE_NAME)
public class AuthUser extends BaseModel implements Serializable, BaseUser {
    private static final long serialVersionUID = -3781190399240349761L;

    public static final String TABLE_NAME = TableLib.CLUB + "USER";
    public static final String FIELD_ACCOUNT = "account";

    /**
     * 用户账号
     * @required
     */
    @Id
    @NotBlank(groups = {Insert.class, Modify.class})
    @Size(max = 50, groups = {Insert.class})
    private String account;

    /**
     * 密码
     */
    @NotBlank(groups = {Modify.class})
    @JsonInclude
    private String password;

    /**
     * 用户名称
     * @required
     */
    @Size(max = 30, groups = {Insert.class, Update.class})
    @NotBlank(groups = {Insert.class, Update.class})
    private String userName;

    /**
     * 用户等级 0访客  1 普通用户 2 vip用户 3 管理员
     * @required
     */
    @Max(value = 3,groups = {Insert.class})
    @Min(value = 1,groups = {Insert.class})
    private int leveles;

    /**
     * 性别：1.男；2.女，保密 0。
     */
    private int gender;

    private String avatar;

    private String email;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 签名
     */
    @Size(max = 300, groups = {Insert.class, Update.class})
    private String descripter;

    @Override
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLeveles() {
        return leveles;
    }

    public void setLeveles(int leveles) {
        this.leveles = leveles;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public int getLevel() {
        return leveles;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescripter() {
        return descripter;
    }

    public void setDescripter(String descripter) {
        this.descripter = descripter;
    }
}


