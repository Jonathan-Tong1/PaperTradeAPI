package com.jt.securetrading.repositories;

import com.jt.securetrading.models.AppRole;
import com.jt.securetrading.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName (AppRole appRole);
}
