package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.responses.RefreshTokenResponse;
import com.ehmjamiu.jobboard.exceptions.CustomBadRequest;
import com.ehmjamiu.jobboard.exceptions.CustomNotFound;
import com.ehmjamiu.jobboard.models.Token;
import com.ehmjamiu.jobboard.models.User;
import com.ehmjamiu.jobboard.repositories.TokenRepository;
import com.ehmjamiu.jobboard.repositories.UserRepository;
import com.ehmjamiu.jobboard.services.JwtService;
import com.ehmjamiu.jobboard.services.TokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public TokenServiceImpl(JwtService jwtService, UserRepository userRepository, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public RefreshTokenResponse getRefreshToken(String accessToken) {
        String email = jwtService.extractEmail(accessToken);
        Token token = tokenRepository.findTokenByUserEmail(email).orElseThrow(()
                -> new CustomNotFound("Refresh token not found"));
        if(!isRefreshTokenValid(token.getRefreshToken())) {
            throw new CustomBadRequest("Refresh token is invalid, please try log in again");
        }
        LocalDateTime refreshTokenExpiresAt = jwtService.expirationDate(token.getRefreshToken());
        LocalDateTime accessTokenExpiresAt = jwtService.expirationDate(accessToken);
        return new RefreshTokenResponse(token.getRefreshToken(), accessToken,
                                        calculateExpiresIn(refreshTokenExpiresAt),
                                        calculateExpiresIn(accessTokenExpiresAt),
                                        token.isRevoked());

    }

    @Override
    public RefreshTokenResponse generateRefreshToken(String email) {
        if(email == null) {
            throw new CustomBadRequest("Email cannot be null");
        }
        String accessToken = jwtService.generateAccessToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);
        LocalDateTime accessTokenExpiresAt = jwtService.expirationDate(accessToken);
        LocalDateTime refreshTokenExpiresAt = jwtService.expirationDate(refreshToken);
        User user = userRepository.findUserByEmail(email).orElseThrow(()
                -> new CustomNotFound("User with email " + email + " not found"));
        if(tokenRepository.existsByUserEmail(email)){
            Token existingToken = tokenRepository.findTokenByUserEmail(email).orElseThrow(()
            -> new CustomNotFound("Token not found"));
            existingToken.setRefreshToken(refreshToken);
            existingToken.setExpiresAt(refreshTokenExpiresAt);
            existingToken.setRevoked(false);
            tokenRepository.save(existingToken);
        }else {
            saveToken(refreshToken, user);
        }
        return new RefreshTokenResponse(refreshToken,
                                        accessToken,
                                        calculateExpiresIn(refreshTokenExpiresAt),
                                        calculateExpiresIn(accessTokenExpiresAt),
                                false);

    }

    @Override
    public void saveToken(String refreshToken, User user) {
        Token token = new Token();
        token.setUser(user);
        token.setRefreshToken(refreshToken);
        token.setIssueAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMonths(6));
        token.setRevoked(false);
        tokenRepository.save(token);

    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken) {
        Optional<Token> token = tokenRepository.findTokenByRefreshToken(refreshToken);
        return token.isPresent() && !token.get().isRevoked() &&token.get().getExpiresAt().isAfter(LocalDateTime.now());

    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        Optional<Token> token = tokenRepository.findTokenByRefreshToken(refreshToken);
        if (token.isPresent()) {
            token.get().setRevoked(true);
            tokenRepository.save(token.get());
        }
    }

    @Override
    public void revokeAndDeleteTokenForUser(Long userId) {
        tokenRepository.deleteTokenByUserId(userId);

    }

    public static long calculateExpiresIn(LocalDateTime expiresAt) {
        return Math.max(0, expiresAt.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }
}