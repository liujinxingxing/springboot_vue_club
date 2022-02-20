package com.club.framework.cache;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public class Cache<V> {

    private Map<String, CacheData<V>> cache = new ConcurrentHashMap<String, CacheData<V>>();

    public CacheData get(String key) {
        return cache.get(key);
    }

    public Set<String> getkeys(){
        return cache.keySet();
    }

    public void put(String key, V value) {
        put(key, value,-1L);
    }

    public void put(String key, V value, long expire) {
        cache.put(key, new CacheData<V>(value, expire));
    }

    public void evict(String key) {
        cache.remove(key);
    }

    public int size() {
        return cache.size();
    }

    public void expire(String key, long expire) {
        CacheData<V> data = cache.get(key);
        if (data != null) {
            data.setExpireTime(Calendar.getInstance().getTimeInMillis() + expire);
            cache.put(key,data);
        }
    }

    public Map<String, CacheData<V>> getCache() {
        return cache;
    }

}
