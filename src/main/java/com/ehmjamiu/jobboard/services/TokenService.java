package com.ehmjamiu.jobboard.services;

import com.ehmjamiu.jobboard.dtos.responses.RefreshTokenResponse;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    RefreshTokenResponse getRefreshToken(String token);

    RefreshTokenResponse generateRefreshToken(String email);
}
