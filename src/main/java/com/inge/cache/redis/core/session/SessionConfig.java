package com.inge.cache.redis.core.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inge.cache.redis.core.config.CacheExtendProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 引入session后，配置session才会生效
 *
 * @author lavyoung1325
 */
@ConditionalOnProperty(prefix = "spring.redis", name = "session")
@Configuration
public class SessionConfig {

    // properties
    CacheExtendProperties cacheExtendProperties;

    /**
     * 构造
     */
    public SessionConfig(CacheExtendProperties cacheExtendProperties) {
        this.cacheExtendProperties = cacheExtendProperties;
    }

    /**
     * session 序列化
     *
     * @return redis序列化
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }

    /**
     * cookie 序列化
     *
     * @return cookie序列化
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookiePath(cacheExtendProperties.getSession().getCookiePath());
        cookieSerializer.setDomainName(cacheExtendProperties.getSession().getDomainName());
        cookieSerializer.setCookieName(cacheExtendProperties.getSession().getCookieName());
        return cookieSerializer;
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
