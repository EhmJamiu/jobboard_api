package com.ehmjamiu.jobboard.dtos.responses;

import com.ehmjamiu.jobboard.models.enums.UserRole;

public record UserInfo(
        Long id,
        String email,
        UserRole role
) {
}
