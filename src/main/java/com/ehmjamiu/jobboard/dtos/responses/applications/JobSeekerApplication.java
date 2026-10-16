package com.ehmjamiu.jobboard.dtos.responses.applications;

import com.ehmjamiu.jobboard.dtos.responses.JobResponse;

public record JobSeekerApplication (
        String resumeUrl,
        String coverLetter,
        JobResponse jobResponse
) {

}