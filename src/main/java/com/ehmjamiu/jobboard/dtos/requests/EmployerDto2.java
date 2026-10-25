package com.ehmjamiu.jobboard.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record EmployerDto2(
        @NotNull @NotBlank @Email
        String email,
        @NotBlank @NotNull
        String companyName,
        String companyDescription,
        String companyLogo,
        String websiteUrl,
        Long userId
) {
}
