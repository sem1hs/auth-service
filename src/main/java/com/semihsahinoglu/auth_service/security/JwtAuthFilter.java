package com.semihsahinoglu.auth_service.security;

import com.semihsahinoglu.auth_service.entity.RefreshToken;
import com.semihsahinoglu.auth_service.service.CustomUserDetailsService;
import com.semihsahinoglu.auth_service.service.JwtService;
import com.semihsahinoglu.auth_service.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthFilter(JwtService jwtService, RefreshTokenService refreshTokenService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String autHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;
        String path = request.getRequestURI();

        if (autHeader != null && autHeader.startsWith("Bearer ")) {
            token = autHeader.substring(7);
            if (path.startsWith("/api/v1/auth/refresh-token") || path.startsWith("/api/v1/auth/logout")) {
                RefreshToken tokenEntity = refreshTokenService.findByToken(token);
                username = tokenEntity.getUser().getUsername();
            } else {
                username = jwtService.extractUsername(token);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            log.info("User yüklendi {}", userDetails.getUsername());
            if (path.startsWith("/api/v1/auth/refresh-token") || path.startsWith("/api/v1/auth/logout")) {
                if (refreshTokenService.validateRefreshToken(token)) {
                    log.info("Token doğrulandı {}", token);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } else if (jwtService.validateToken(token, userDetails)) {
                log.info("Token doğrulandı {}", token);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
