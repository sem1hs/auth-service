package com.semihsahinoglu.auth_service.service;

import com.semihsahinoglu.auth_service.entity.User;
import com.semihsahinoglu.auth_service.repository.UserRepository;
import com.semihsahinoglu.auth_service.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        return new CustomUserDetails(user);
    }
}
