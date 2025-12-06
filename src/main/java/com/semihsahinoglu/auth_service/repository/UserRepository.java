package com.semihsahinoglu.auth_service.repository;

import com.semihsahinoglu.auth_service.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"authorities"})
    Optional<User> findByUsername(String username);
}
