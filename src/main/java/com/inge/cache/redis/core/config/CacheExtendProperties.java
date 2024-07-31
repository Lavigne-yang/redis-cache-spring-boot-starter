package com.inge.cache.redis.core.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "spring.redis"
)
public class CacheExtendProperties extends RedisProperties {

    /**
     * KEY 前缀
     */
    private String prefix;

    private boolean redisson;
    /**
     * session配置
     */
    private SessionExtendProperties session;


    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isRedisson() {
        return redisson;
    }

    public void setRedisson(boolean redisson) {
        this.redisson = redisson;
    }

    public SessionExtendProperties getSession() {
        return session;
    }

    public void setSession(SessionExtendProperties session) {
        this.session = session;
    }

    public static class SessionExtendProperties {

        private String cookiePath;

        private String domainName;

        private String cookieName;

        public String getCookiePath() {
            return cookiePath;
        }

        public void setCookiePath(String cookiePath) {
            this.cookiePath = cookiePath;
        }

        public String getDomainName() {
            return domainName;
        }

        public void setDomainName(String domainName) {
            this.domainName = domainName;
        }

        public String getCookieName() {
            return cookieName;
        }

        public void setCookieName(String cookieName) {
            this.cookieName = cookieName;
        }
    }
}
