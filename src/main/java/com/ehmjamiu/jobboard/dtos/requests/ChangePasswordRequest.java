package com.ehmjamiu.jobboard.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "Old password must not be blank")
        String oldPassword,

        @NotBlank(message = "New password cannot be blank")
        String newPassword,

        @Email(message = "Enter appropriate email")
        String email) {
}
