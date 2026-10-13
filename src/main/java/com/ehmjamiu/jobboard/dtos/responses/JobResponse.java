package com.ehmjamiu.jobboard.dtos.responses;

import com.ehmjamiu.jobboard.models.enums.JobStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record JobResponse(
        Long id,
        String companyName,
        String companyDescription,
        String websiteUrl,
        String title,
        String description,
        String location,
        String category,
        JobStatus status,
        Double salary,
        LocalDateTime postedAt,
        LocalDateTime expiredAt
) {


}