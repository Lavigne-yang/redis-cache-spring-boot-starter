package com.inge.cache.redis.core;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * redis拓展配置
 *
 * @author lavyoung1325
 */
@Configuration
@ConfigurationProperties(prefix = "inge.cache")
public class RedisExtendConfig {

    /**
     * redis key前缀
     */
    private String prefix = "KEY";

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
