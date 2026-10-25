package com.ehmjamiu.jobboard.services;

import com.ehmjamiu.jobboard.dtos.requests.LoginRequest;
import com.ehmjamiu.jobboard.dtos.requests.RegisterUserRequest;
import com.ehmjamiu.jobboard.dtos.responses.AuthResponse;
import com.ehmjamiu.jobboard.models.enums.UserRole;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    AuthResponse registerUser(@Valid RegisterUserRequest user, HttpServletResponse response);

    AuthResponse loginUser(@Valid LoginRequest user, HttpServletResponse response);

    void changePassword(@NotBlank(message = "Old password must not be blank") String s, @NotBlank(message = "New password cannot be blank") String s1, @Email(message = "Enter appropriate email") String email);

    void changeUserRole(String email, UserRole role);


}
