package com.club.base.model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 * 基础model
 * @model 基础model
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public class BaseModel {

    /**
     * 最后更新用户id
     */
    protected String updateUserId;

    /**
     * 最后更新用户名称
     */
    protected String updateUserName;

    /**
     * 最后更新时间
     */
    protected Date updateTime;

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
