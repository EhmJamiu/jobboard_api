package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.responses.ResetTokenResponse;
import com.ehmjamiu.jobboard.exceptions.CustomBadRequest;
import com.ehmjamiu.jobboard.exceptions.UserDoesNotExist;
import com.ehmjamiu.jobboard.models.ResetToken;
import com.ehmjamiu.jobboard.models.User;
import com.ehmjamiu.jobboard.repositories.ResetTokenRepository;
import com.ehmjamiu.jobboard.repositories.UserRepository;
import com.ehmjamiu.jobboard.services.ResetTokenService;
import com.ehmjamiu.jobboard.utils.CalculateExpiresTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class ResetTokenServiceImpl implements ResetTokenService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final UserRepository userRepository;
    private final ResetTokenRepository resetTokenRepository;

    public ResetTokenServiceImpl(UserRepository userRepository, ResetTokenRepository resetTokenRepository) {
        this.userRepository = userRepository;
        this.resetTokenRepository = resetTokenRepository;
    }


    @Override
    public ResetTokenResponse generateInitialPasswordReset(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(()
                -> new UserDoesNotExist("User not found"));
        String token = UUID.randomUUID().toString();
        Optional<ResetToken> existingResetToken = resetTokenRepository.findResetTokenByUserEmail(email);
        if (existingResetToken.isPresent()) {
            ResetToken resetToken = existingResetToken.get();
            return new ResetTokenResponse(resetToken.getToken(),
                    CalculateExpiresTime.calculateExpiresIn(resetToken.getExpiresAt()),resetToken.getUser().getEmail());
        }
        ResetToken resetToken = new ResetToken();
        resetToken.setToken(UUID.fromString(token));
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        resetTokenRepository.save(resetToken);

        return new ResetTokenResponse(resetToken.getToken(),
                CalculateExpiresTime.calculateExpiresIn(resetToken.getExpiresAt()),user.getEmail());


    }

    @Override
    public void resetPassword(String resetPasswordToken, String newPassword) {
        ResetToken reset_token = resetTokenRepository.findResetTokensByToken(UUID.fromString(resetPasswordToken)).orElseThrow(()
                -> new CustomBadRequest("Invalid or expired reset token"));
        if (reset_token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomBadRequest("Reset token expired");
        }
        reset_token.getUser().setPassword(encoder.encode(newPassword));
        resetTokenRepository.delete(reset_token);
    }
}