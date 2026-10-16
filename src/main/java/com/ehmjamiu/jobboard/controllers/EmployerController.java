package com.ehmjamiu.jobboard.controllers;

import com.ehmjamiu.jobboard.dtos.requests.JobRequest;
import com.ehmjamiu.jobboard.services.JobService;
import com.ehmjamiu.jobboard.utils.responseHandler.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/employer")
//@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerController {

    private final JobService jobService;

    public EmployerController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/job")
    public ResponseEntity<?> postJob(@ModelAttribute JobRequest request){
        //String authHeader = token.substring(7);
        String email = jwtService.extractEmail(authHeader);
        jobService.postJob(request, email);

        return ResponseHandler.handleResponse("", HttpStatus.CREATED, "A job posted");

    }

    @PutMapping("/job/{id}")
    public ResponseEntity<?> postJob(@ModelAttribute JobRequest request, @PathVariable Long id){
//        String authHeader = token.substring(7);
        String email = jwtService.extractEmail(authHeader);
        jobService.updateJob(request, id);

        return ResponseHandler.handleResponse("", HttpStatus.OK, "A job updated");

    }

    @DeleteMapping("/job/{id}")
    public ResponseEntity<?> postJob(@PathVariable Long id ){
//        String authHeader = token.substring(7);
        String email = jwtService.extractEmail(authHeader);
        jobService.deleteJob(id, email);

        return ResponseHandler.handleResponse("", HttpStatus.OK, "A job deleted");

    }




}