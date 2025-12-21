package com.semihsahinoglu.auth_service.dto;

import com.semihsahinoglu.auth_service.entity.Role;

import java.util.Set;

public record UserResponse(
        String username,
        Set<Role> roles
) {
}
