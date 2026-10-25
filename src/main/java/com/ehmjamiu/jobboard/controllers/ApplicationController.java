package com.ehmjamiu.jobboard.controllers;

import com.ehmjamiu.jobboard.dtos.requests.ApplicationRequest;
import com.ehmjamiu.jobboard.dtos.responses.applications.JobSeekerApplication;
import com.ehmjamiu.jobboard.dtos.responses.applications.ApplicationResponse;
import com.ehmjamiu.jobboard.models.enums.ApplicationStatus;
import com.ehmjamiu.jobboard.services.ApplicationService;
import com.ehmjamiu.jobboard.services.JwtService;
import com.ehmjamiu.jobboard.utils.responseHandler.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationController {
    private final JwtService jwtService;

    private  final ApplicationService applicationService;

    public ApplicationController(JwtService jwtService, ApplicationService applicationService) {
        this.jwtService = jwtService;
        this.applicationService = applicationService;
    }

    @PostMapping("/jobs/{id}/apply")
    @PreAuthorize("hasRole('JOB_SEEKEER')")
    public ResponseEntity<?> applyJob(@PathVariable Long jobId, @RequestHeader("Authorization") String authHeader,
                                      @ModelAttribute ApplicationRequest jobApplicationRequest) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        applicationService.applyForJob(jobId,jobApplicationRequest, email);
        return ResponseHandler.handleResponse("", HttpStatus.OK,"Successfully applied for a job");
    }

    @DeleteMapping("jobs/{applicationId}/{jobId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<?> deleteJobSeekerApplication(@PathVariable Long applicationId, @PathVariable Long jobId,@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        applicationService.deleteApplication(applicationId,jobId,email);
        return ResponseHandler.handleResponse("", HttpStatus.OK,"Successfully deleted application");
    }

    @GetMapping("/job-seeker/applications")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<?> getAllApplicationsByJobSeeker(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        List<JobSeekerApplication> response = applicationService.getAllMyApplicationsForJobSeeker(email);
        return ResponseHandler.handleResponse(response, HttpStatus.OK,"Successfully retrieved applications");
    }

    @GetMapping("/jobs/applications")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<?> getAllApplicationsByEmployee(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        List<ApplicationResponse> responseList =applicationService.getAllApplicationsByEmployee(email);
        return ResponseHandler.handleResponse(responseList, HttpStatus.OK,"Successfully retrieved applications");
    }

    @GetMapping("/jobs/{jobId}/applications")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<?> getAllApplicationsByJob(@PathVariable Long jobId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        List<ApplicationResponse> responseList = applicationService.getAllApplicationsForJobIdByEmployee(email, jobId);
        return ResponseHandler.handleResponse(responseList, HttpStatus.OK,"Successfully retrieved applications");
    }

    @PutMapping("/jobs/{applicationId}/{jobSeekerId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<?> updateApplication(@PathVariable Long applicationId,
                                               @PathVariable Long jobSeekerId,
                                               @RequestHeader("Authorization") String authHeader,
                                               @RequestBody ApplicationStatus status){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        applicationService.updateApplicationStatus(applicationId,jobSeekerId,email,status);
        return ResponseHandler.handleResponse("", HttpStatus.OK,"Successfully updated application");
    }

}