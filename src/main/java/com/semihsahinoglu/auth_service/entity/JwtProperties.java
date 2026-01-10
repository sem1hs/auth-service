package com.semihsahinoglu.auth_service.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private int accessTokenExpiration;
    private int refreshTokenExpiration;
    private int serviceTokenExpiration;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(int accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public int getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(int refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public int getServiceTokenExpiration() {
        return serviceTokenExpiration;
    }

    public void setServiceTokenExpiration(int serviceTokenExpiration) {
        this.serviceTokenExpiration = serviceTokenExpiration;
    }
}
