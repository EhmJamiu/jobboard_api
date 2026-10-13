package com.ehmjamiu.jobboard.dtos.requests;

import com.ehmjamiu.jobboard.models.enums.JobStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record JobRequest(
        @NotBlank(message = "Title cannot be blank") @NotNull(message = "Title is required")
        String title,
        @NotBlank(message = "Description cannot be blank") @NotNull(message = "Description is required")
        String description,
        String location,
        String category,
        JobStatus status,
        Double salary,
        Long employerId,
        LocalDateTime postedAt,
        LocalDateTime expiredAt
) {
}
