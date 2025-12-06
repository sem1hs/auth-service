package com.semihsahinoglu.auth_service.dto;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CreateUserRequest(
        @NotBlank(message = "Kullanıcı adı boş olamaz !")
        String username,

        @NotBlank(message = "Şifre boş olamaz !")
        String password,

        Set<String> authorities
) {
}
