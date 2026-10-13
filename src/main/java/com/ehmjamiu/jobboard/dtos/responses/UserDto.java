package com.ehmjamiu.jobboard.dtos.responses;

import com.ehmjamiu.jobboard.models.enums.UserRole;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String email,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
