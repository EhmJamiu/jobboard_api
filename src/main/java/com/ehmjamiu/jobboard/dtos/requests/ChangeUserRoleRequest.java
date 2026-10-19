package com.ehmjamiu.jobboard.dtos.requests;

import com.ehmjamiu.jobboard.models.enums.UserRole;

public record ChangeUserRoleRequest(
        String email,
        UserRole role
) {
}
