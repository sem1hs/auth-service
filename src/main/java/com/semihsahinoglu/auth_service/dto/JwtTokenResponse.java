package com.semihsahinoglu.auth_service.dto;

public record JwtTokenResponse(
        String accessToken,
        String refreshToken
) implements ApiResponse {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String accessToken;
        private String refreshToken;

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public JwtTokenResponse build() {
            return new JwtTokenResponse(accessToken, refreshToken);
        }
    }
}
