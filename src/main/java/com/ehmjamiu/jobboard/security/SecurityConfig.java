package com.ehmjamiu.jobboard.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.print.attribute.standard.Media;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //private final UserDetailsService userDetailsService;

    private final JwtFilter filter;



    public SecurityConfig(JwtFilter filter) {
        //this.userDetailsService = userDetailsService;
        this.filter = filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/hello", "/swagger-ui/index.html", "/v3/api-docs",
                                "/api/auth/**", "/api/jobs", "/api/jobs/{id}","/api/jobs/**", "/api/auth/register",
                                        //"/oauth2/**",
                                "/login/**",
                                "/oauth/**","/api/unsubscribe/**").permitAll()
                                //job_seeker
                                .requestMatchers("/api/jobs-seeker/**","/api/jobs/{id}/apply","/api/jobs/{id}/save","/api/user/job-seeker/**").hasRole("JOB_SEEKER")
                                //employer
                                .requestMatchers("/api/employer/**", "/api/jobs/{id}/applications").hasRole("EMPLOYER")
                                //admin
                                .requestMatchers("/api/admin/**").hasRole("ADMIN").anyRequest().authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(
                        (request, response, authException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            Map<String, Object> responseBody = new HashMap<>();
                            responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
                            responseBody.put("message", authException.getMessage());
                            responseBody.put("data", null);
                            new ObjectMapper().writeValue(response.getOutputStream(),responseBody);
                        })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    Map<String, Object> responseBody = new HashMap<>();
                                    responseBody.put("status", HttpStatus.FORBIDDEN.value());
                                    responseBody.put("message", accessDeniedException.getMessage() + "Access denied - insufficient privileges" );
                                    responseBody.put("data", null);
                                    new ObjectMapper().writeValue(response.getOutputStream(), responseBody);

                                })
                        )
                .httpBasic(Customizer.withDefaults())
//                    .oauth2Login(oauth2->oauth2
//                            .userInfoEndpoint(info->info
//                                    .userService(customOAuth2UserService))
//                            .successHandler(oauth2SuccessHandler))
                    .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                    .build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }
}