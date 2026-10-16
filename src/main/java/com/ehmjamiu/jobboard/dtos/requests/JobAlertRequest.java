package com.ehmjamiu.jobboard.dtos.requests;

import com.ehmjamiu.jobboard.models.enums.AlertFrequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JobAlertRequest(
        @NotNull @NotBlank(message = "Job title must be set") String searchedQuery,
        AlertFrequency frequency
) {

}