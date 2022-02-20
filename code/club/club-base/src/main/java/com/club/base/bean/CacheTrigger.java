package com.club.base.bean;

/**
 * CacheTrigger的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public interface CacheTrigger {

    void triggerKey(String key);

    void triggerCache(String key, String cache);
}
