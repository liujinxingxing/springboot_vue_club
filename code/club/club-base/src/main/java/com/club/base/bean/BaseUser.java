package com.club.base.bean;

/**
 * 用户基本字段 接口描述
 * @model 用户基本字段
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public interface BaseUser {

    /**
     * 用户账号 手机
     */
    String getAccount();

    /**
     * 用户姓名
     */
    String getUserName();

    /**
     * 用户邮箱
     */
    String getEmail();

    /**
     * 用户头像
     */
    String getAvatar();

    /**
     * 用户等级
     */
    int getLevel();

}
