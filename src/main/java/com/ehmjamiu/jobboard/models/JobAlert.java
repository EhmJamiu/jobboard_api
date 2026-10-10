package com.ehmjamiu.jobboard.models;

import com.ehmjamiu.jobboard.models.enums.AlertFrequency;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class JobAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeeker jobSeeker;

    @Column(nullable = false)
    private String searchedQuery;

    @Enumerated(EnumType.STRING)
    private AlertFrequency frequency=AlertFrequency.DAILY;


}