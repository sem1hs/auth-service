package com.semihsahinoglu.auth_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class User extends Auditable{

    @NotBlank(message = "Kullanıcı adı boş olamaz !")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Şifre boş olamaz !")
    private String password;

    private boolean accountNonExpired;
    private boolean isEnabled;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> authorities = new HashSet<>();

    public User() {
    }

    public User(String username,
                String password,
                boolean accountNonExpired,
                boolean isEnabled,
                boolean accountNonLocked,
                boolean credentialsNonExpired,
                Set<Role> authorities
    ) {
        this.username = username;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.isEnabled = isEnabled;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.authorities = authorities;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String username;
        private String password;
        private boolean accountNonExpired;
        private boolean isEnabled;
        private boolean accountNonLocked;
        private boolean credentialsNonExpired;
        private Set<Role> authorities = new HashSet<>();

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder accountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public Builder isEnabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }

        public Builder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public Builder credentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public Builder authorities(Set<Role> authorities) {
            this.authorities = authorities;
            return this;
        }

        public User build() {
            return new User(username, password, accountNonExpired, isEnabled, accountNonLocked, credentialsNonExpired, authorities);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean getIsAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean getIsCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }
}
