package com.quadrangle.capstone.pill_navigator.repositories;

import com.quadrangle.capstone.pill_navigator.entities.RefreshToken;
import com.quadrangle.capstone.pill_navigator.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    @Modifying
    void deleteRefreshTokenByUser(User user);
}
