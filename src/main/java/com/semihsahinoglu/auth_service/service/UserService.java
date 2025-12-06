package com.semihsahinoglu.auth_service.service;

import com.semihsahinoglu.auth_service.dto.CreateUserRequest;
import com.semihsahinoglu.auth_service.entity.Role;
import com.semihsahinoglu.auth_service.entity.User;
import com.semihsahinoglu.auth_service.exception.AuthorityNotFoundException;
import com.semihsahinoglu.auth_service.exception.UserNotFoundException;
import com.semihsahinoglu.auth_service.repository.RoleRepository;
import com.semihsahinoglu.auth_service.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User createUser(CreateUserRequest createUserRequest) {
        Set<Role> authorities = createUserRequest.authorities().stream().
                map(role -> roleRepository.findRoleByName(role).orElseThrow(() -> new AuthorityNotFoundException("Kullanıcı Rolü Geçersiz !")))
                .collect(Collectors.toSet());


        User user = User.builder()
                .username(createUserRequest.username())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .isEnabled(true)
                .accountNonLocked(true)
                .authorities(authorities)
                .build();

        return userRepository.save(user);
    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException("Kullanıcı bulunamadı !"));
    }
}
