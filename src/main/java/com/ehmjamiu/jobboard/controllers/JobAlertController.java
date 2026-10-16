package com.ehmjamiu.jobboard.controllers;


import com.ehmjamiu.jobboard.dtos.requests.JobAlertRequest;
import com.ehmjamiu.jobboard.services.JobAlertService;
import com.ehmjamiu.jobboard.utils.responseHandler.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
//@PreAuthorize("hasRole('')")
public class JobAlertController {

    private final JobAlertService jobAlertService;

    public JobAlertController(JobAlertService jobAlertService) {
        this.jobAlertService = jobAlertService;
    }


    @PostMapping("/job-alert")
    public ResponseEntity<?> setJobAlerts(@RequestBody JobAlertRequest alert, @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        String email = jwtService.extractEmail(token);
        jobAlertService.setJobAlert(alert, email);
        return ResponseHandler.handleResponse(null, HttpStatus.OK,"Successfully set job alerts");
    }


    @DeleteMapping("job-alert/{id}")
    public ResponseEntity<?> deleteJobAlerts(@PathVariable("id") Long jobAlertId, @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        String email = jwtService.extractEmail(token);
        jobAlertService.deleteJobAlert(jobAlertId, email);
        return ResponseHandler.handleResponse(null, HttpStatus.OK,"Successfully deleted");
    }
}