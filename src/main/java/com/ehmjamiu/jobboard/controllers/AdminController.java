package com.ehmjamiu.jobboard.controllers;

import com.ehmjamiu.jobboard.dtos.responses.AllUserResponse;
import com.ehmjamiu.jobboard.services.UserDataService;
import com.ehmjamiu.jobboard.utils.responseHandler.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserDataService userDataService;

    public AdminController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping("/all/user-data")
    public ResponseEntity<?> getAllUserData (){
        AllUserResponse response = userDataService.getAllUserData();
        return ResponseHandler.handleResponse(response, HttpStatus.CREATED, "All user with their data");
    }

    @GetMapping("/all/users")
    public ResponseEntity<?> getAllUser (){
        AllUserResponse response = userDataService.getAllUser();
        return ResponseHandler.handleResponse(response, HttpStatus.CREATED, "Get all users");
    }

    @GetMapping("/all/job-seeker")
    public ResponseEntity<?> getAllJobSeeker(){
        AllUserResponse response = userDataService.getAllJobSeeker();
        return ResponseHandler.handleResponse(response, HttpStatus.CREATED, "All job seekers data");
    }

    @GetMapping("/all/employers")
    public  ResponseEntity<?> getAllEmployer(){
        AllUserResponse response = userDataService.getAllEmployer();
        return ResponseHandler.handleResponse(response, HttpStatus.CREATED, "All employer data");
    }

    @DeleteMapping("/job-seeker/{id}")
    public ResponseEntity<?> deleteJobSeeker(@PathVariable Long id){
        userDataService.deleteJobSeekerById(id);
        return ResponseHandler.handleResponse(null, HttpStatus.CREATED, "Job seeker with id: "+id+ " deleted");
    }

    @DeleteMapping("/employer/{id}")
    public ResponseEntity<?> deleteEmployer(@PathVariable Long id){
        userDataService.deleteEmployerById(id);
        return ResponseHandler.handleResponse(null, HttpStatus.CREATED, "Job employer with id: "+id+ " deleted");
    }


}