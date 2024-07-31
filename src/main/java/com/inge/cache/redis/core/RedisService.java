package com.inge.cache.redis.core;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 核心服务，对缓存内容进行操作
 * RedisTemplate<String, Object>
 * <dl>
 *    1. 操作
 * </dl>
 *
 * @author lavyoung1325
 */
@SuppressWarnings("unused")
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 构造注入
     *
     */
    RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }



    /**
     * 设置缓存
     *
     * @param key   缓存key {@link String}
     * @param value 缓存对应的对象的 class 对象
     * @param <T>   or null
     */
    public <T> void setValue(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @param key    缓存key
     * @param value  对象值
     * @param time 失效时间
     * @param unit 单位
     */
    public <T> void setValue(String key, T value, long time, TimeUnit unit) {
        if (key == null || key.isEmpty()) {
            return;
        }
        redisTemplate.opsForValue().set(key, value, time, unit);
    }

    /**
     * @param key   缓存key
     * @param value 对象值
     * @param from  随机时间 开始范围内
     * @param to    随机时间  结束范围
     * @param unit  单位
     */
    public <T> void setValue(String key, T value, int from, int to, TimeUnit unit) {
        if (key == null || key.isEmpty()) {
            return;
        }
        redisTemplate.opsForValue().set(key, value, new Random().nextInt(to - from) + from, unit);
    }

    /**
     * 占锁
     *
     * @param key
     * @param value
     * @param time
     * @param unit
     */
    public <T> boolean setValueIfAbsent(String key, T value, long time, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, time, unit));
    }

    /**
     * 占锁
     *
     * @param key
     * @param value
     * @param <T>
     */
    public <T> boolean setValueIfAbsent(String key, T value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return
     */
    private Object getValue(String key) {
        if (!Optional.ofNullable(redisTemplate.hasKey(key)).orElse(Boolean.FALSE)) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * Description: 取值
     *
     * @param key   缓存 {@link String}
     * @param clazz 缓存对应的对象的 class 对象
     * @return T or null
     * @see RedisService#getValue(String)
     */
    public <T> T getValue(String key, Class<T> clazz) {
        return clazz.cast(getValue(key));
    }


    /**
     * 删除
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }


    /**
     * 通过脚本删除 指定锁及值 （原子性）
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> Long deleteByScript(String key, T value) {
        // 获取锁的值
        // 删除锁时需要保证是删除自己加 定义lua 脚本
        // 定义lua 脚本
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        return redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(key), value);
    }

    /**
     * 指定key设置过期时间
     *
     * @param key
     * @param time
     * @param unit
     */
    public void expire(String key, long time, TimeUnit unit) {
        redisTemplate.expire(key, time, unit);
    }
}
