package com.ehmjamiu.jobboard.dtos.responses;

import com.ehmjamiu.jobboard.models.enums.UserRole;

public record JobSeekerDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        String resumeUrl,
        String skills,
        String experiences
) {

}