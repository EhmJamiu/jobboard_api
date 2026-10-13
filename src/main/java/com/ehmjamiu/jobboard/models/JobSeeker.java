package com.ehmjamiu.jobboard.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "job_seekers")
public class JobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Email @Column(nullable = false) @NotBlank(message="Email cannot be blank")
    String email;

    private String firstName;

    private String lastName;

    private String resumeUrl;

    private String skills;

    private String experience;


}