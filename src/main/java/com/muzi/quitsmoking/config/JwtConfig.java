package com.muzi.quitsmoking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * 密钥
     */
    private String secret = "defaultSecretKeyMustBeAtLeast32BytesLongForHS256";

    /**
     * 过期时间（毫秒）
     */
    private long expiration = 86400000; // 默认24小时

    /**
     * Token前缀
     */
    private String tokenPrefix = "Bearer ";

    /**
     * Token头部名称
     */
    private String headerName = "Authorization";

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }
} 