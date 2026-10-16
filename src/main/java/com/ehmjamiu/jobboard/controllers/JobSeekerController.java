package com.ehmjamiu.jobboard.controllers;

import com.ehmjamiu.jobboard.dtos.responses.JobId;
import com.ehmjamiu.jobboard.dtos.responses.SavedJobResponse;
import com.ehmjamiu.jobboard.models.SavedJob;
import com.ehmjamiu.jobboard.services.SavedJobService;
import com.ehmjamiu.jobboard.utils.responseHandler.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
//PreAuthorize("hasRole('JOB_SEEKER')")
public class JobSeekerController {
    private final SavedJobService savedJobService;

    public JobSeekerController(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }


    @GetMapping("/saved-job")
    public ResponseEntity<?> getAllSavedJobByAJobSeeker(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        SavedJobResponse response = savedJobService.getAllSavedJobByJobSeeker(email);
        return ResponseHandler.handleResponse(response, HttpStatus.CREATED, "Saved jobs retrieved");
    }


    @GetMapping("/saved-job/{id}")
    public ResponseEntity<?> getSavedJobById(@RequestHeader("Authorization") String authHeader, @PathVariable Long id){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        SavedJobResponse response = savedJobService.getSavedJobById(id, email);
        return ResponseHandler.handleResponse(response, HttpStatus.CREATED, "A job retrieved");
    }

    // won't there suppose be path variable in the endpoint
    @PostMapping("saved-job/{jobId}")
    public ResponseEntity<?> saveJob(@RequestHeader("Authorization") String authHeader, @PathVariable JobId jobId){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        SavedJobResponse response = savedJobService.saveJob(jobId.id(), email);
        return ResponseHandler.handleResponse(response, HttpStatus.CREATED, "A job saved");
    }

    @DeleteMapping("/saved-job/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable("id") Long savedJobId, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        savedJobService.deleteJob(savedJobId, email);
        return ResponseHandler.handleResponse(null, HttpStatus.OK, "A job unsaved");

    }




}