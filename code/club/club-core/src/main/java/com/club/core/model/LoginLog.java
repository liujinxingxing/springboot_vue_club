package com.club.core.model;

import com.club.core.constant.TableLib;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * LoginLog的包装类描述
 * @model 登录日志
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
@Table(name = LoginLog.TABLE_NAME)
public class LoginLog {

    public static final String TABLE_NAME = TableLib.CLUB + "USER_LOGIN_LOG";
    public static final String FIELD_UUID = "uuid";

    /**
     * 登录会话id
     */
    @Id
    private String uuid;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 自动退出 true 手动退出 false  超时退出
     */
    private Boolean quiter;

    /**
     * 登录时间
     */
    private Date startTime;

    /**
     * 退出时间
     */
    private Date finishTime;

    /**
     * 用户名称
     */
    @Transient
    private String userName;

    /**
     * 用户等级 0访客  1 普通用户 2 vip用户 3 管理员
     */
    @Transient
    private int leveles;

    /**
     * 性别：1.男；2.女，保密 0。
     */
    @Transient
    private int gender;

    /**
     * 邮箱
     */
    @Transient
    private String email;

    public LoginLog() {
    }

    public LoginLog(String uuid, String account) {
        this.uuid = uuid;
        this.account = account;
        startTime = new Date();
    }

    public LoginLog(String uuid, Boolean quiter) {
        this.uuid = uuid;
        this.quiter = quiter;
        finishTime = new Date();
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getQuiter() {
        return quiter;
    }

    public void setQuiter(Boolean quiter) {
        this.quiter = quiter;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
