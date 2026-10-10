package com.ehmjamiu.jobboard.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.EmbeddedColumnNaming;

import java.time.LocalDateTime;

@Entity
@Data
public class SavedJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "jobSeeker_id")
    private JobSeeker jobSeeker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "job_id")
    private Job job;

    @CreationTimestamp
    private LocalDateTime savedAt;




}