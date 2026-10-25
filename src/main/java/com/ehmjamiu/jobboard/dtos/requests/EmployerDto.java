package com.ehmjamiu.jobboard.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record EmployerDto(

        @NotNull @NotBlank @Email
        String email,
        @NotBlank @NotNull
        String companyName,
        String companyDescription,
        MultipartFile companyLogo,
        String websiteUrl,
        Long userId

) {
}
