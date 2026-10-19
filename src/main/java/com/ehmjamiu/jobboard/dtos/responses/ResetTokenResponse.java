package com.ehmjamiu.jobboard.dtos.responses;

import java.util.UUID;

public record ResetTokenResponse(
        UUID resetToken,
        long expiresAt,
        String email
) {
}
