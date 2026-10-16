package com.ehmjamiu.jobboard.dtos.responses.applications;

public record JobSeekerApplied(
        String email,
        String firstName,
        String lastName,
        String skills
) {
}
