package com.ehmjamiu.jobboard.dtos.responses;

public record EmployerResponse(
        Long id,
        String companyName,
        String companyDescription,
        String logoUrl,
        String websiteUrl,
        String email
) {
}
