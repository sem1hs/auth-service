package com.semihsahinoglu.auth_service.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends Auditable {

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private Instant expiryDate;
        private User user;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder expiryDate(Instant expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public RefreshToken build() {
            return new RefreshToken(token, expiryDate, user);
        }
    }

    public RefreshToken() {
    }

    public RefreshToken(String token, Instant expiryDate, User user) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
