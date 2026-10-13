package com.ehmjamiu.jobboard.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record JobSeekerRequest(
        @NotNull @NotBlank @Email String email,
        @NotNull @NotBlank String firstName,
        @NotNull @NotBlank String lastName,
        MultipartFile resumeFile,
        String skill,
        String experience,
        Long userId
) {
}
