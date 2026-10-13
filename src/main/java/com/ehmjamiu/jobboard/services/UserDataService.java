package com.ehmjamiu.jobboard.services;

import com.ehmjamiu.jobboard.dtos.requests.EmployerDto;
import com.ehmjamiu.jobboard.dtos.requests.EmployerRequest;
import com.ehmjamiu.jobboard.dtos.requests.JobSeekerRequest;
import com.ehmjamiu.jobboard.dtos.responses.AllUserResponse;
import com.ehmjamiu.jobboard.dtos.responses.EmployerResponse;
import com.ehmjamiu.jobboard.dtos.responses.JobSeekerResponse;
import com.ehmjamiu.jobboard.models.JobSeeker;
import org.springframework.stereotype.Service;

@Service
public interface UserDataService {

    //Job seeker endpoints
    JobSeekerResponse addJobSeeker(JobSeekerRequest jobSeeker);
    JobSeekerResponse updateJobSeeker(JobSeekerRequest jobSeeker, Long id);
    JobSeekerResponse findJobSeekerByEmail(String email);

    //Employer endpoints
    EmployerResponse addEmployer(EmployerDto employer);
    EmployerResponse updateEmployer(EmployerRequest employer, Long id);
    EmployerResponse findEmployerByEmail(String email);

    //Admin endpoints
    AllUserResponse getAllUserData();
    AllUserResponse getAllUser();
    AllUserResponse getAllJobSeeker();
    AllUserResponse getAllEmployer();
    void deleteJobSeekerById(Long id);
    void deleteEmployerById(Long id);
}