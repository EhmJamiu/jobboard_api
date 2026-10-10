package com.ehmjamiu.jobboard.models;

import jakarta.persistence.*;
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

    private String firstName;

    private String lastName;

    private String resumeUrl;

    private String skills;

    private String experiences;


}