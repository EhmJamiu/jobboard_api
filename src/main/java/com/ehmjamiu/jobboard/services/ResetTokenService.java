package com.ehmjamiu.jobboard.services;

import com.ehmjamiu.jobboard.dtos.responses.ResetTokenResponse;
import org.springframework.stereotype.Service;

@Service
public interface ResetTokenService {

    ResetTokenResponse generateInitialPasswordReset(String email);

    void resetPassword(String resetToken, String newPassword);
}