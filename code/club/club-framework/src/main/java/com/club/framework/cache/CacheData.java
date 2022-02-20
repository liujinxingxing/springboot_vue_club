package com.club.framework.cache;

import java.util.Calendar;

/**
 * CacheData的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public class CacheData<T> {
    // 缓存数据
    private T data;
    // 过期时间(单位，毫秒)
    private long expireTime;

    public CacheData(T t, long expire) {
        this.data = t;
        if (expire < 0) {
            this.expireTime = -1L;
        } else {
            this.expireTime = Calendar.getInstance().getTimeInMillis() + expire;
        }
    }

    /**
     * 判断缓存数据是否过期
     *
     * @return true表示过期，false表示未过期
     */
    public boolean isExpire() {
        if (expireTime < 0) {
            return false;
        }
        if (expireTime > Calendar.getInstance().getTimeInMillis()) {
            return false;
        }
        return true;
    }

    public T getData() {
        return data;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
