package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.requests.LoginRequest;
import com.ehmjamiu.jobboard.dtos.requests.RegisterUserRequest;
import com.ehmjamiu.jobboard.dtos.responses.AuthResponse;
import com.ehmjamiu.jobboard.dtos.responses.UserInfo;
import com.ehmjamiu.jobboard.exceptions.InvalidEntity;
import com.ehmjamiu.jobboard.exceptions.UserAlreadyExist;
import com.ehmjamiu.jobboard.models.User;
import com.ehmjamiu.jobboard.models.enums.UserRole;
import com.ehmjamiu.jobboard.repositories.UserRepository;
import com.ehmjamiu.jobboard.services.JwtService;
import com.ehmjamiu.jobboard.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    @Override
    public AuthResponse registerUser(RegisterUserRequest user, HttpServletResponse response) {
        Optional<User> existingUser = userRepository.findUserByEmail(user.email());
        if (existingUser.isPresent()){
            throw new UserAlreadyExist(existingUser.get().getEmail() + "is already exist");
        }

        UserRole role;
        if(user.role() == null) {
            role = UserRole.JOB_SEEKER;
        } else {
            try {
                role = UserRole.valueOf(user.role().toString().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidEntity("Invalid role " + user.role());
            }
        }

        User newUser = new User();
        newUser.setEmail(user.email());
        newUser.setRole(user.role());
        newUser.setPassword(encoder.encode(user.password()));
        newUser.setCreatedAt(LocalDateTime.now());

        userRepository.save(newUser);

        String accessToken;
        accessToken = jwtService.generateAccessToken(user.email());
        handleTokenGeneration(newUser, response);
        return new AuthResponse(accessToken, new UserInfo(newUser.getId(), newUser.getEmail(), newUser.getRole()));
    }



    @Override
    public AuthResponse loginUser(LoginRequest user, HttpServletResponse response) {
        return null;
    }

    @Override
    public void changePassword(String s, String s1, String email) {

    }

    @Override
    public void changeUserRole(String email, UserRole role) {

    }

    public void handleTokenGeneration(User user, HttpServletResponse response) {


    }
}