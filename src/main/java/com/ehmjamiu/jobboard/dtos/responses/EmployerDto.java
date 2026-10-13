package com.ehmjamiu.jobboard.dtos.responses;

import com.ehmjamiu.jobboard.models.enums.UserRole;

public record EmployerDto(
        Long id,
        String email,
        UserRole role,
        String companyName,
        String companyDescription,
        String logoUrl,
        String websiteUrl
) {
}
