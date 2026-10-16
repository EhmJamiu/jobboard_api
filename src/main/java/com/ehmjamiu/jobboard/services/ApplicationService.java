package com.ehmjamiu.jobboard.services;

import com.ehmjamiu.jobboard.dtos.requests.ApplicationRequest;
import com.ehmjamiu.jobboard.dtos.responses.applications.ApplicationResponse;
import com.ehmjamiu.jobboard.dtos.responses.applications.JobSeekerApplication;
import com.ehmjamiu.jobboard.models.enums.ApplicationStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApplicationService {

    void applyForJob(Long jobId, ApplicationRequest jobApplicationRequest, String email);

    void deleteApplication(Long applicationId, Long jobId, String email);

    List<JobSeekerApplication> getAllMyApplicationsForJobSeeker(String email);

    List<ApplicationResponse> getAllApplicationsByEmployee(String email);

    List<ApplicationResponse> getAllApplicationsForJobIdByEmployee(String email, Long jobId);

    void updateApplicationStatus(Long applicationId, Long jobSeekerId, String email, ApplicationStatus status);
}