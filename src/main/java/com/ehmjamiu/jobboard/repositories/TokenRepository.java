package com.ehmjamiu.jobboard.repositories;

import com.ehmjamiu.jobboard.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findTokenByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);

    Optional<Token> findTokenByRefreshToken(String refreshToken);

    void deleteTokenByUserId(Long userId);
}
