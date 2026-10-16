package com.ehmjamiu.jobboard.models;

import com.ehmjamiu.jobboard.models.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(nullable = false)
    private JobSeeker jobSeeker;

    @Column(nullable = false)
    private String resumeUrl;

    private String coverLetter;

    @Column(nullable = false)
    private ApplicationStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

}