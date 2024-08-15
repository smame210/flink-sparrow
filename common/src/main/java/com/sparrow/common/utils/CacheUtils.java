package com.sparrow.common.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存
 *
 * @author daitf
 * @date 2024/6/24
 */
public class CacheUtils {
    private CacheUtils() {
    }

    /**
     * 默认过期时长
     */
    public static final long NOT_EXPIRE = Long.MAX_VALUE;

    private static final Cache<String, CacheObject<?>> CAFFEINE = Caffeine.newBuilder()
            .expireAfter(new Expiry<String, CacheObject<?>>() {
                @Override
                public long expireAfterCreate(@NonNull String key, @NonNull CacheObject<?> value, long currentTime) {
                    return value.expire;
                }

                @Override
                public long expireAfterUpdate(@NonNull String key, @NonNull CacheObject<?> value, long currentTime, @NonNegative long currentDuration) {
                    return value.expire;
                }

                @Override
                public long expireAfterRead(@NonNull String key, @NonNull CacheObject<?> value, long currentTime, @NonNegative long currentDuration) {
                    return value.expire;
                }
            })
            .initialCapacity(100)
            .maximumSize(1024)
            .build();

    private static class CacheObject<T> {
        T data;
        long expire;

        public CacheObject(T data, long second) {
            this.data = data;
            this.expire = TimeUnit.SECONDS.toNanos(second);
        }
    }

    public static <T> void set(String key, T value, long expire) {
        CacheObject<T> cacheObject = new CacheObject<>(value, expire);
        CAFFEINE.put(key, cacheObject);
    }

    public static void set(String key, Object value) {
        set(key, value, NOT_EXPIRE);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        CacheObject<?> cacheObject = CAFFEINE.getIfPresent(key);
        if (Objects.isNull(cacheObject)) {
            return null;
        }
        return (T) cacheObject.data;
    }

    public static void delete(String key) {
        CAFFEINE.invalidate(key);
    }

    public static void delete(Collection<String> keys) {
        CAFFEINE.invalidateAll(keys);
    }
}
