package com.semihsahinoglu.auth_service.service;

import com.semihsahinoglu.auth_service.dto.CreateUserRequest;
import com.semihsahinoglu.auth_service.dto.JwtTokenResponse;
import com.semihsahinoglu.auth_service.dto.LoginRequest;
import com.semihsahinoglu.auth_service.entity.RefreshToken;
import com.semihsahinoglu.auth_service.entity.User;
import com.semihsahinoglu.auth_service.exception.AuthenticationFailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public JwtTokenResponse loginAndGenerateToken(LoginRequest loginRequest) {
        String accessToken = null;
        RefreshToken refreshToken = null;
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

            if (authentication.isAuthenticated()) {
                User user = userService.findUserByUsername(loginRequest.username());
                accessToken = jwtService.generateAccessToken(loginRequest.username());
                refreshToken = refreshTokenService.generateRefreshToken(user);
            }
        } catch (AuthenticationException e) {
            throw new AuthenticationFailException("Kullanıcı adı veya şifre hatalı !");
        }

        return buildTokenResponse(accessToken, refreshToken.getToken());
    }

    public JwtTokenResponse signUpAndGenerateToken(CreateUserRequest createUserRequest) {
        User user = userService.createUser(createUserRequest);

        String accessToken = jwtService.generateAccessToken(user.getUsername());
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user);

        return buildTokenResponse(accessToken, refreshToken.getToken());
    }

    public void logout(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenString);
        refreshTokenService.deleteToken(refreshToken);
    }

    public JwtTokenResponse refreshAllTokens(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(refreshTokenString);
        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateAccessToken(user.getUsername());
        RefreshToken newRefresh = refreshTokenService.generateRefreshToken(user);

        return buildTokenResponse(newAccessToken, newRefresh.getToken());
    }

    private JwtTokenResponse buildTokenResponse(String accessToken, String refreshToken) {
        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
