package com.ehmjamiu.jobboard.dtos.responses;

public record JobSeekerResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String resumeFile,
        String skill,
        String experience
) {
}
