package com.ehmjamiu.jobboard.repositories;

import com.ehmjamiu.jobboard.models.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long > {

    Optional<ResetToken> findResetTokenByUserEmail(String userEmail);

    Optional<ResetToken> findResetTokensByToken(UUID token);
}