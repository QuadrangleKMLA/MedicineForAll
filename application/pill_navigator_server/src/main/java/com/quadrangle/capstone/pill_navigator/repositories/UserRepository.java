package com.quadrangle.capstone.pill_navigator.repositories;

import com.quadrangle.capstone.pill_navigator.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);
    Boolean existsUserByUsername(String username);
}
