package com.ehmjamiu.jobboard.controllers;

import com.ehmjamiu.jobboard.dtos.requests.EmployerDto;
import com.ehmjamiu.jobboard.dtos.requests.EmployerRequest;
import com.ehmjamiu.jobboard.dtos.requests.JobSeekerRequest;
import com.ehmjamiu.jobboard.dtos.responses.EmployerResponse;
import com.ehmjamiu.jobboard.dtos.responses.JobResponse;
import com.ehmjamiu.jobboard.dtos.responses.JobSeekerResponse;
import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.services.JwtService;
import com.ehmjamiu.jobboard.services.UserDataService;
import com.ehmjamiu.jobboard.utils.responseHandler.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final JwtService jwtService;

    private final UserDataService userDataService;

    public UserController(JwtService jwtService, UserDataService userDataService) {
        this.jwtService = jwtService;
        this.userDataService = userDataService;
    }

    @PostMapping(value = "/job-seeker", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveJobSeeker(@ModelAttribute JobSeekerRequest jobSeeker) {
        JobSeekerResponse response =  userDataService.addJobSeeker(jobSeeker);
        return ResponseHandler.handleResponse(response, HttpStatus.CREATED, "Job Seeker Detail Created");
    }


    @PutMapping(value = "/job-seeker/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateJobSeeker(@ModelAttribute JobSeekerRequest jobSeeker, @PathVariable Long id) {
        JobSeekerResponse response =  userDataService.updateJobSeeker(jobSeeker, id);
        return ResponseHandler.handleResponse(response, HttpStatus.OK, "Job Seeker Detail Updated");
    }

    //Authorization required
    @GetMapping("job-seeker")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<?> getJobSeeker(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        JobSeekerResponse response =  userDataService.findJobSeekerByEmail(email);
        return ResponseHandler.handleResponse(response, HttpStatus.OK, "Job Seeker Data");

    }

    @PostMapping(value = "/employer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveEmployer(@ModelAttribute EmployerDto employer){
        EmployerResponse response = userDataService.addEmployer(employer);

        return ResponseHandler.handleResponse(response, HttpStatus.CREATED, "Employer Detail Created");
    }

    @PutMapping(value = "/employer/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveEmployer(@ModelAttribute EmployerRequest employer, @PathVariable Long id){
        EmployerResponse response = userDataService.updateEmployer(employer, id);

        return ResponseHandler.handleResponse(response, HttpStatus.OK, "Employer Detail Updated");
    }

    @GetMapping("employer")
    public  ResponseEntity<?> getEmployer(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        EmployerResponse response = userDataService.findEmployerByEmail(email);
        return ResponseHandler.handleResponse(response, HttpStatus.OK, "Employer Data");
    }


}