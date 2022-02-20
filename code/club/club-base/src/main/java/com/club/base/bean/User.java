package com.club.base.bean;

/**
 * User的包装类描述
 * @model 用户回话
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public class User implements BaseUser {

    private String account;

    private String userName;
    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 用户等级 0访客  1 普通用户 2 vip用户 3 管理员
     */
    private int level;

    private String avatar;

    private String email;

    /**
     * 性别：1.男；2.女，保密 0。
     */
    private int gender;

    /**
     * 领域
     */
    private String realm;

    /**
     * 签名
     */
    private String descripter;

    /**
     * 创建时间
     */
    private long createTime;

    public User() {
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getDays() {
        if(createTime > 0){
            return (int) ((System.currentTimeMillis() - createTime))/(3600000*24);
        }
        return 0;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isAdmin() {
        return 3 == level;
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

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDescripter() {
        return descripter;
    }

    public void setDescripter(String descripter) {
        this.descripter = descripter;
    }
}
