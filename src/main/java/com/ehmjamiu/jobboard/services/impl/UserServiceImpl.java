package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.requests.LoginRequest;
import com.ehmjamiu.jobboard.dtos.requests.RegisterUserRequest;
import com.ehmjamiu.jobboard.dtos.responses.AuthResponse;
import com.ehmjamiu.jobboard.dtos.responses.UserInfo;
import com.ehmjamiu.jobboard.exceptions.CustomBadRequest;
import com.ehmjamiu.jobboard.exceptions.InvalidEntity;
import com.ehmjamiu.jobboard.exceptions.UserAlreadyExist;
import com.ehmjamiu.jobboard.exceptions.UserDoesNotExist;
import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.models.User;
import com.ehmjamiu.jobboard.models.enums.UserRole;
import com.ehmjamiu.jobboard.repositories.EmployerRepository;
import com.ehmjamiu.jobboard.repositories.JobSeekerRepository;
import com.ehmjamiu.jobboard.repositories.UserRepository;
import com.ehmjamiu.jobboard.services.JwtService;
import com.ehmjamiu.jobboard.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private final EmployerRepository employerRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(EmployerRepository employerRepository, JobSeekerRepository jobSeekerRepository, UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.employerRepository = employerRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    @Override
    @Transactional
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
    @Transactional
    public AuthResponse loginUser(LoginRequest user, HttpServletResponse response) {
        User existingUser = userRepository.findUserByEmail(user.email()).orElseThrow(()
                -> new UserDoesNotExist("This user is yet to register"));
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.email(), user.password()));
            if (authentication.isAuthenticated()) {
                String accessToken = jwtService.generateAccessToken(existingUser.getEmail());
                handleTokenGeneration(existingUser,response);
                return new AuthResponse(accessToken,new UserInfo(existingUser.getId(),existingUser.getEmail(),existingUser.getRole()));
            }else {
                return new AuthResponse(null,null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(()->
                new UserDoesNotExist("User does not exist"));
        if(oldPassword==null||newPassword==null){
            throw new CustomBadRequest("Old password or new password can not be null");
        }

        if(!encoder.matches(oldPassword, user.getPassword())){
            throw new CustomBadRequest("Old password does not match");
        }
        String encodedNewPassword = encoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void changeUserRole(String email, UserRole role) {
        User user = userRepository.findUserByEmail(email).orElseThrow(()->
                new UserDoesNotExist("User does not exist"));
        UserRole currentRole = user.getRole();
        if(currentRole!=role){
            switch (currentRole){
                case JOB_SEEKER:
                    jobSeekerRepository.deleteByUser(user);
                     break;
                case EMPLOYER:
                    employerRepository.deleteByUser(user);
                    break;
                default:
                    break;
            }
        }
        user.setRole(role);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void handleTokenGeneration(User user, HttpServletResponse response) {


    }
}