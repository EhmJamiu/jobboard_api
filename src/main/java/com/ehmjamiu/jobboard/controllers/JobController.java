package com.ehmjamiu.jobboard.controllers;

import com.ehmjamiu.jobboard.services.JobAlertService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JobController {
    private final JobAlertService jobAlertService;

    public JobController(JobAlertService jobAlertService) {
        this.jobAlertService = jobAlertService;
    }


    @GetMapping("/unsubscribe/{id}/{email}")
    public ResponseEntity<?> unsubscribeFromJobAlerts(@PathVariable Long id, @PathVariable String email) {
        jobAlertService.deleteJobAlert(id,email);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML)
                .body("<html>" +
                        "<body style='display:flex;justify-items:center;align-items:center'>" +
                        "<h1>You've successfully unsubscribe from Job board Job Alerts</h1>" +
                        "</body>" +
                        "</html>");

    }

}