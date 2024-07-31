package com.inge.cache.redis.core.redisson;

import com.inge.cache.redis.core.config.CacheExtendProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author lavyoung1325
 */
@Component
@ConditionalOnProperty(prefix = "spring.redis", name = "redisson")
public class RedissonConfig {

    private final CacheExtendProperties cacheExtendProperties;

    @Autowired
    public RedissonConfig(CacheExtendProperties cacheExtendProperties) {
        this.cacheExtendProperties = cacheExtendProperties;
    }

    /**
     * redisson客户端bean注入
     * 通过redissonClient对象使用redisson
     *
     * @return
     */
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisUrl = String.format("redis://%s:%s", cacheExtendProperties.getHost(), cacheExtendProperties.getPort());
        config.useSingleServer().setAddress(redisUrl).setPassword(cacheExtendProperties.getPassword());
        config.useSingleServer().setConnectionMinimumIdleSize(10);
        return Redisson.create(config);
    }
}
