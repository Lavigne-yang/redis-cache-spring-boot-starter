package com.inge.cache.redis.core.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 引入session才会生效
 *
 * @author lavyoung1325
 */
@ConditionalOnClass(RedisHttpSessionConfiguration.class)
@Configuration
public class SessionConfig {

    /**
     * session 序列化
     *
     * @return
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }

    /**
     * cookie 序列化
     *
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookiePath("/");
        cookieSerializer.setDomainName("lavyoung.com");
        cookieSerializer.setCookieName("GMSESSION");
        return cookieSerializer;
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
