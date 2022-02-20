package com.club.framework.cache;

import com.club.base.bean.CacheTrigger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * CacheManager的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public class CacheManager {

    @SuppressWarnings("rawtypes")
    private static Map<String, Cache> CACHE_TOP = new ConcurrentHashMap<String, Cache>();

    private static CacheTrigger TRIGGER;

    /**
     * 启动定时任务清理过期缓存，避免内存溢出
     */
    static {
        Timer t = new Timer();
        t.schedule(new ClearTimerTask(CACHE_TOP), 0, 60 * 1000);
    }

    public static void setTrigger(CacheTrigger trigger) {
        TRIGGER = trigger;
    }

    public static Set<String> getCacheNames() {
        return CACHE_TOP.keySet();
    }

    public static Set<String> getCachKeys(String cacheName) {
        if (CACHE_TOP.containsKey(cacheName)) {
            return CACHE_TOP.get(cacheName).getkeys();
        }
        return new HashSet<>();
    }

    public static <T> T get(String cacheName, String key, Class<T> clazz) {
        if (CACHE_TOP.containsKey(cacheName)) {
            Cache cache = CACHE_TOP.get(cacheName);
            CacheData cacheData = cache.get(key);
            if (cacheData != null) {
                if (cacheData.isExpire()) {
                    cache.evict(key);
                    return null;
                }
                Object data = cacheData.getData();
                if (data != null && clazz.isAssignableFrom(data.getClass())) {
                    return (T) data;
                }
            }
        }
        return null;
    }

    public static void expire(String cacheName, String key, long expire) {
        expire(cacheName, key, expire, TimeUnit.MILLISECONDS);
    }

    public static void expire(String cacheName, String key, long expire, TimeUnit unit) {
        switch (unit) {
            case DAYS:
                expire *= 24;
            case HOURS:
                expire *= 60;
            case MINUTES:
                expire *= 60;
            case SECONDS:
                expire *= 1000;
                break;
        }
        if (CACHE_TOP.containsKey(cacheName)) {
            CACHE_TOP.get(cacheName).expire(key, expire);
        }
    }

    public static <T> void put(String cacheName, String key, Object value, long expire, TimeUnit unit) {
        switch (unit) {
            case DAYS:
                expire *= 24;
            case HOURS:
                expire *= 60;
            case MINUTES:
                expire *= 60;
            case SECONDS:
                expire *= 1000;
                break;
        }
        Cache cache = CACHE_TOP.get(cacheName);
        if (cache == null) {
            cache = new Cache<>();
        }
        cache.put(key, value, expire);
        CACHE_TOP.put(cacheName, cache);
    }

    public static <T> void put(String cacheName, String key, Object value, long expire) {
        put(cacheName, key, value, expire, TimeUnit.MILLISECONDS);
    }

    public static <T> void put(String cacheName, String key, Object value) {
        put(cacheName, key, value, -1L);
    }

    public static void evict(String cacheName, String key) {
        if (CACHE_TOP.containsKey(cacheName)) {
            CACHE_TOP.get(cacheName).evict(key);
        }
    }

    public static void clear(String cacheName) {
        CACHE_TOP.remove(cacheName);
    }

    public static void clear() {
        CACHE_TOP.clear();
    }

    /**
     * 清理过期数据定时任务
     */
    private static class ClearTimerTask extends TimerTask {

        Map<String, Cache> cache;

        public ClearTimerTask(Map<String, Cache> cache) {
            this.cache = cache;
        }

        @Override
        public void run() {
            for (Map.Entry<String, Cache> entry : cache.entrySet()) {
                Cache value = entry.getValue();
                scanEexpire(entry.getKey(), value);
                if (value.size() == 0) {
                    trigger(true, entry.getKey());
                    cache.remove(entry.getKey());
                }
            }
        }

        protected void trigger(boolean key, String... value) {
            if (TRIGGER != null) {
                if (key) {
                    TRIGGER.triggerKey(value[0]);
                    return;
                }
                TRIGGER.triggerCache(value[0], value[1]);
            }
        }

        protected void scanEexpire(String key, Cache cache) {
            Map<String, CacheData<Object>> tmp = cache.getCache();
            for (Map.Entry<String, CacheData<Object>> entry : tmp.entrySet()) {
                if (entry.getValue().isExpire()) {
                    trigger(false, key, entry.getKey());
                    cache.evict(entry.getKey());
                }
            }
        }
    }
}
