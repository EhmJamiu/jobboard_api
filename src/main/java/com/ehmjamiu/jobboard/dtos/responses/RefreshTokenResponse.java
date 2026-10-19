package com.ehmjamiu.jobboard.dtos.responses;

public record RefreshTokenResponse(
        String refreshToken,
        String accessToken,
        Long refreshTokenExpiresAt,
        Long accessTokenExpiresAt,
        boolean revoked
) {
}
