package com.jt.securetrading.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jt.securetrading.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String username);
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
