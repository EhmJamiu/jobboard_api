package com.ehmjamiu.jobboard.dtos.requests;

import com.ehmjamiu.jobboard.models.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @Email(message="Enter a valid email")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 5, message = "Password should be at least more than 5 characters")
        String password,

        UserRole role
) {
}
