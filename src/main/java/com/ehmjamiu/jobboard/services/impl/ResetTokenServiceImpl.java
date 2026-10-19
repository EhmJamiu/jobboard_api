package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.responses.ResetTokenResponse;
import com.ehmjamiu.jobboard.services.ResetTokenService;
import org.springframework.stereotype.Service;


@Service
public class ResetTokenServiceImpl implements ResetTokenService {

    @Override
    public ResetTokenResponse generateInitialPasswordReset(String email) {
        return null;
    }

    @Override
    public void resetPassword(String resetToken, String newPassword) {

    }
}