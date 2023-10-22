package com.quadrangle.capstone.pill_navigator.repositories;

import com.quadrangle.capstone.pill_navigator.entities.ERole;
import com.quadrangle.capstone.pill_navigator.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByRole(ERole role);
    Boolean existsRoleByRole(ERole role);
}
