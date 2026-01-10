package com.semihsahinoglu.auth_service.controller;

import com.semihsahinoglu.auth_service.service.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/auth")
public class InternalAuthController {

    private final JwtService jwtService;

    public InternalAuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/service-token")
    public String generateServiceToken(@RequestHeader("X-Service-Name") String serviceName) {
        return jwtService.generateServiceToken(serviceName);
    }
}