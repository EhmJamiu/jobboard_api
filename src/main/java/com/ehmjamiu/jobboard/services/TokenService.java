package com.ehmjamiu.jobboard.services;

import com.ehmjamiu.jobboard.dtos.responses.RefreshTokenResponse;
import com.ehmjamiu.jobboard.models.User;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    RefreshTokenResponse getRefreshToken(String token);

    RefreshTokenResponse generateRefreshToken(String email);

    void saveToken(String refreshToken, User user);
    boolean isRefreshTokenValid(String refreshToken);
    void revokeRefreshToken(String refreshToken);
    void revokeAndDeleteTokenForUser(Long userId);



}
