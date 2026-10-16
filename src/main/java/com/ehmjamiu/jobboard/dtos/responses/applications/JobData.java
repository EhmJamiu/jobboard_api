package com.ehmjamiu.jobboard.dtos.responses.applications;

import com.ehmjamiu.jobboard.models.enums.JobStatus;

import java.time.LocalDateTime;

public record JobData(
        String companyName,
        String websiteUrl,
        JobStatus jobStatus,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Double salary,
        String category){
}
