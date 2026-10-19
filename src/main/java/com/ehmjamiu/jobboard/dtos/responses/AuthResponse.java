package com.ehmjamiu.jobboard.dtos.responses;

public record AuthResponse(
        String accessToken,
        UserInfo user
) {
}
