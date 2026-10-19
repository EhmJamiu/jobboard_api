package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.responses.RefreshTokenResponse;
import com.ehmjamiu.jobboard.services.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public RefreshTokenResponse getRefreshToken(String token) {
        return null;
    }

    @Override
    public RefreshTokenResponse generateRefreshToken(String email) {
        return null;
    }
}