package com.ehmjamiu.jobboard.controllers;

import com.ehmjamiu.jobboard.dtos.requests.JobRequest;
import com.ehmjamiu.jobboard.services.JobService;
import com.ehmjamiu.jobboard.services.JwtService;
import com.ehmjamiu.jobboard.utils.responseHandler.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/employer")
//@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerController {

    private final JobService jobService;
    private final JwtService jwtService;

    public EmployerController(JobService jobService, JwtService jwtService) {
        this.jobService = jobService;
        this.jwtService = jwtService;
    }

    @PostMapping("/job")
    public ResponseEntity<?> postJob(@RequestHeader("Authorization") String authHeader, @ModelAttribute JobRequest request){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(authHeader);
        jobService.postJob(request, email);

        return ResponseHandler.handleResponse("", HttpStatus.CREATED, "A job posted");

    }

    @PutMapping("/job/{id}")
    public ResponseEntity<?> postJob(@RequestHeader("Authorization") String authHeader, @ModelAttribute JobRequest request, @PathVariable Long id){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        jobService.updateJob(request, id);

        return ResponseHandler.handleResponse("", HttpStatus.OK, "A job updated");

    }

    @DeleteMapping("/job/{id}")
    public ResponseEntity<?> postJob(@RequestHeader("Authorization") String authHeader, @PathVariable Long id ){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        jobService.deleteJob(id, email);

        return ResponseHandler.handleResponse("", HttpStatus.OK, "A job deleted");

    }




}