package com.ehmjamiu.jobboard.dtos.requests;

public record ResetTokenDto(
        String resetToken,
        String newPassword
) {
}
