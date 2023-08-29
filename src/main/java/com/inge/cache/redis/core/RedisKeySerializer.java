package com.inge.cache.redis.core;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import reactor.util.annotation.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 实现key统一前缀
 *
 * @author lavyoung1325
 */
public class RedisKeySerializer implements RedisSerializer<String> {


    /**
     * Redis key 的前缀
     */
    private final RedisExtendConfig redisExtendConfig;

    public RedisKeySerializer(RedisExtendConfig redisExtendConfig) {
        this.redisExtendConfig = redisExtendConfig;
    }

    /**
     * 默认UTF-8
     */
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    @Override
    public byte[] serialize(@Nullable String key) throws SerializationException {
        if (key == null || key.isEmpty()) {
            return new byte[0];
        } else {
            String prefix = redisExtendConfig.getPrefix();
            if (prefix == null || prefix.isEmpty()) {
                return key.getBytes(CHARSET);
            } else {
                return (prefix + ":" + key).getBytes(CHARSET);
            }
        }
    }

    @Override
    public String deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        } else {
            String prefix = redisExtendConfig.getPrefix();
            if (prefix == null || prefix.isEmpty()) {
                return new String(bytes, CHARSET);
            } else {
                return new String(bytes, CHARSET).replaceFirst(prefix + ":", "");
            }
        }
    }
}
