package com.inge.cache.redis.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * 自定义了简单的配置, 实现序列化和反序列化.
 *
 * @author lavyoung1325
 */
@Configuration
@ConditionalOnClass(RedisService.class)
@ComponentScan("com.inge.cache.redis")
@EnableCaching
@EnableConfigurationProperties({RedisExtendConfig.class, CacheProperties.class})
public class RedisServiceAutoConfigure {

    RedisKeySerializer keyRedisSerializer;

    RedisConnectionFactory redisConnectionFactory;

    RedisExtendConfig redisExtendConfig;
    /**
     * 构造注入
     *
     * @param redisExtendConfig 额外增加的redis配置
     */
    @Autowired
    public RedisServiceAutoConfigure(RedisExtendConfig redisExtendConfig, RedisConnectionFactory redisConnectionFactory) {
        keyRedisSerializer = new RedisKeySerializer(redisExtendConfig);
        this.redisExtendConfig = redisExtendConfig;
        this.redisConnectionFactory = redisConnectionFactory;
    }


    /**
     * @return redis核心业务
     * @ConditionalOnMissingBean, 当上下文中不存在该 Bean 时才创建
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisService redisService() {
        return new RedisService(redisTemplate());
    }

    /**
     * redis值序列化
     *
     * @return
     */
    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> valueRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(new DefaultBaseTypeLimitingValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        valueRedisSerializer.serialize(objectMapper);
        return valueRedisSerializer;
    }

    /**
     * 缓存管理器
     *
     * @param redisConnectionFactory
     * @param cacheProperties
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, CacheProperties cacheProperties) {
        // RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        // value序列化器
        RedisSerializationContext.SerializationPair<Object> serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer());
        RedisSerializationContext.SerializationPair<String> cacheKeySerialization = RedisSerializationContext.SerializationPair.fromSerializer(keyRedisSerializer);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(serializationPair)
                .serializeKeysWith(cacheKeySerialization);
        CacheProperties.Redis redis = cacheProperties.getRedis();
        if (redis != null) {
            // redis cache的配置暂时只用过期时间
            // redisCacheConfiguration = redisCacheConfiguration.entryTtl(redis.getTimeToLive());
        }
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> objectRedisTemplate = new RedisTemplate<>();
        // 配置连接工厂
        objectRedisTemplate.setConnectionFactory(redisConnectionFactory);
        // 值序列化-RedisFastJsonSerializer
        objectRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
        objectRedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer());
        // 键序列化-StringRedisSerializer
        objectRedisTemplate.setKeySerializer(keyRedisSerializer);
        objectRedisTemplate.setHashKeySerializer(keyRedisSerializer);
        objectRedisTemplate.afterPropertiesSet();
        return objectRedisTemplate;
    }
}
