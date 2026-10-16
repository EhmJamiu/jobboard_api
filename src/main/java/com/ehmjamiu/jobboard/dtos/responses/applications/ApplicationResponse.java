package com.ehmjamiu.jobboard.dtos.responses.applications;

import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.models.enums.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationResponse (

        String resumeUrl,
        String coverLetter,
        LocalDateTime appliedAt,
        ApplicationStatus applicationStatus,
        JobSeeker jobSeeker,
        JobData jobData

) {

}