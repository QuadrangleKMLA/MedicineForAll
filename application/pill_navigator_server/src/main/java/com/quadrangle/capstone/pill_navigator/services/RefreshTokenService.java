package com.quadrangle.capstone.pill_navigator.services;

import com.quadrangle.capstone.pill_navigator.entities.RefreshToken;
import com.quadrangle.capstone.pill_navigator.exceptions.exceptionClass.ResourceNotFoundException;
import com.quadrangle.capstone.pill_navigator.exceptions.exceptionClass.TokenRefreshException;
import com.quadrangle.capstone.pill_navigator.repositories.RefreshTokenRepository;
import com.quadrangle.capstone.pill_navigator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${quadrangle.app.jwtRefreshExpirationSec}")
    private int refreshTokenDurationSec;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findRefreshTokenByToken(token);
    }

    public RefreshToken createRefreshToken(Integer id) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setId(id);
        refreshToken.setUser(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found.")));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationSec));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh Token was expired. Please make a new login request.");
        }

        return token;
    }

    @Transactional
    public void deleteByUsername(String username) {
        refreshTokenRepository.deleteRefreshTokenByUser(userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found.")));
    }
}
