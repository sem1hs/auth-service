package com.semihsahinoglu.auth_service.controller;

import com.semihsahinoglu.auth_service.dto.*;
import com.semihsahinoglu.auth_service.security.CustomUserDetails;
import com.semihsahinoglu.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal CustomUserDetails user) {
        return authService.getUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtTokenResponse token = authService.loginAndGenerateToken(loginRequest);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody CreateUserRequest createUserRequest) {
        JwtTokenResponse token = authService.signUpAndGenerateToken(createUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        authService.logout(refreshToken.refreshToken());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        JwtTokenResponse token = authService.refreshAllTokens(request.refreshToken());
        return ResponseEntity.ok().body(token);
    }
}
