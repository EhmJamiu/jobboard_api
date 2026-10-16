package com.ehmjamiu.jobboard.dtos.responses;

import java.time.LocalDateTime;

public record SavedJobResponse(
        Long savedJobId,
        LocalDateTime savedAt,
        JobResponse jobsData
) {
}
