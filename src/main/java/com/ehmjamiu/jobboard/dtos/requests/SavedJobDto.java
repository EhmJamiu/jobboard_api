package com.ehmjamiu.jobboard.dtos.requests;

import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.JobSeeker;

import java.time.LocalDateTime;

public record SavedJobDto(
        Job job,
        JobSeeker jobSeeker,
        LocalDateTime savedAt

) {
}