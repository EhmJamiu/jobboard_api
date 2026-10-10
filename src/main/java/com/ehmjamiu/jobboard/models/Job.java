package com.ehmjamiu.jobboard.models;

import com.ehmjamiu.jobboard.models.enums.JobStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    @Column(nullable = false)
    private String title;

    private String description;

    private String location;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private Double salary;

    @CreationTimestamp
    private LocalDateTime postedAt;

    private LocalDateTime expiredAt;


    public void updateJobStatus(){
        if(expiredAt != null && LocalDateTime.now().isAfter(this.expiredAt)){
            status = JobStatus.EXPIRED;
        }

    }
}