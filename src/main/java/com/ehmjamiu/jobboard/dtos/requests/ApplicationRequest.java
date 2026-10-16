package com.ehmjamiu.jobboard.dtos.requests;

import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.models.enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record ApplicationRequest(
        @NotBlank @NotNull
    MultipartFile resumeUrl,
    String coverLetter,
    Job job,
    JobSeeker jobSeeker,
    ApplicationStatus status,
    LocalDateTime appliedAt

) {

}