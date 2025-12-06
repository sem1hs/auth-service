package com.semihsahinoglu.auth_service.service;

import com.semihsahinoglu.auth_service.entity.JwtProperties;
import com.semihsahinoglu.auth_service.entity.RefreshToken;
import com.semihsahinoglu.auth_service.entity.User;
import com.semihsahinoglu.auth_service.exception.RefreshTokenNotFoundException;
import com.semihsahinoglu.auth_service.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(JwtProperties jwtProperties, RefreshTokenRepository refreshTokenRepository) {
        this.jwtProperties = jwtProperties;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken generateRefreshToken(User user) {
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpiration()))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token bulunamadı"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token süresi dolmuş");
        }

        return refreshToken;
    }

    public Boolean validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token bulunamadı"));

        if (refreshToken.getExpiryDate().isAfter(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token süresi dolmuş");
        }

        return true;
    }

    public RefreshToken findByToken(String token){
        return refreshTokenRepository.findByToken(token).orElseThrow(()->new RefreshTokenNotFoundException("Refresh Token bulunamadı !"));
    }

    public void deleteToken(RefreshToken token){
        refreshTokenRepository.delete(token);
    }
}
