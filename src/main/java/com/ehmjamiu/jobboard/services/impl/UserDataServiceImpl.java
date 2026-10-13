package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.requests.EmployerDto;
import com.ehmjamiu.jobboard.dtos.requests.EmployerRequest;
import com.ehmjamiu.jobboard.dtos.requests.JobSeekerRequest;
import com.ehmjamiu.jobboard.dtos.responses.EmployerResponse;
import com.ehmjamiu.jobboard.dtos.responses.JobSeekerResponse;
import com.ehmjamiu.jobboard.mapper.EmployerMapper;
import com.ehmjamiu.jobboard.mapper.JobSeekerMapper;
import com.ehmjamiu.jobboard.models.Employer;
import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.models.User;
import com.ehmjamiu.jobboard.repositories.EmployerRepository;
import com.ehmjamiu.jobboard.repositories.JobSeekerRepository;
import com.ehmjamiu.jobboard.repositories.UserRepository;
import com.ehmjamiu.jobboard.services.CloudinaryUploadService;
import com.ehmjamiu.jobboard.services.UserDataService;
import org.springframework.stereotype.Service;
import com.ehmjamiu.jobboard.exceptions.*;

import java.util.Optional;

@Service
public class UserDataServiceImpl implements UserDataService {
    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final JobSeekerMapper jobSeekerMapper;
    private final CloudinaryUploadService cloudinaryUploadService;
    private final EmployerMapper employerMapper;

    public UserDataServiceImpl(EmployerRepository employerRepository, UserRepository userRepository, JobSeekerRepository jobSeekerRepository, JobSeekerMapper jobSeekerMapper, CloudinaryUploadService cloudinaryUploadService, EmployerMapper employerMapper) {
        this.employerRepository = employerRepository;
        this.userRepository = userRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.jobSeekerMapper = jobSeekerMapper;
        this.cloudinaryUploadService = cloudinaryUploadService;
        this.employerMapper = employerMapper;
    }


    @Override
    public JobSeekerResponse addJobSeeker(JobSeekerRequest jobSeeker) {
        //role validation but not yet

        if (jobSeeker.email() == null || jobSeeker.firstName() == null || jobSeeker.lastName() == null) {
            throw new CustomBadRequest("Enter appropriate data");
        }
        User user = userRepository.findByEmail(jobSeeker.email());
        if (user == null) throw new UserDoesNotExist("User is not authenticated yet");

        Optional<JobSeeker> existingJobSeeker = jobSeekerRepository.findJobSeekerByEmail(jobSeeker.email());
        JobSeeker newJobSeeker = existingJobSeeker.orElseGet(JobSeeker::new);

        if(jobSeeker.resumeFile() != null && !jobSeeker.resumeFile().isEmpty()){
            cloudinaryUploadService.uploadFile(String.valueOf(jobSeeker.resumeFile()), "resumeFile");
        }

        JobSeeker saveJobSeeker = jobSeekerRepository.save(newJobSeeker) ;
        return jobSeekerMapper.toJobSeekerResponse(saveJobSeeker);

    }

    @Override
    public JobSeekerResponse updateJobSeeker(JobSeekerRequest jobSeeker, Long id) {
        //role validation but not yet


        if(id ==null || jobSeeker.firstName() == null || jobSeeker.lastName() == null || jobSeeker.email() == null){
            throw new CustomBadRequest("Enter appropriate data");
        }
        JobSeeker existingJobSeeker = (JobSeeker) jobSeekerRepository.findJobSeekerById(id).orElseThrow(()
        -> new CustomNotFound("Job Seeker with id: "+id+ "does not exist"));
        if(jobSeeker.resumeFile() !=null && !jobSeeker.resumeFile().isEmpty()){
            cloudinaryUploadService.uploadFile(String.valueOf(jobSeeker.resumeFile()), "resumeFile");
        }
        JobSeeker savedJobSeeker = jobSeekerRepository.save(existingJobSeeker);
        return jobSeekerMapper.toJobSeekerResponse(savedJobSeeker);
    }

    @Override
    public EmployerResponse addEmployer(EmployerDto employer) {
        //role validation but not yet
    if(employer.companyName() == null || employer.email() == null) {
        throw new CustomBadRequest("Enter appropriate data"); }
    User user = userRepository.findByEmail(employer.email());
    if (user == null) throw new UserDoesNotExist("User is not authenticated yet");

    Optional<Employer> existingEmployer = employerRepository.findByUser(user);
    if(existingEmployer.isPresent()) {
        Employer currentEmployer = existingEmployer.get();
        return new EmployerResponse(currentEmployer.getId(), currentEmployer.getCompanyName(), currentEmployer.getCompanyDescription(),
                currentEmployer.getLogoUrl(), currentEmployer.getWebsiteUrl(), currentEmployer.getUser().getEmail());
    }
    if(employer.companyLogo() != null && !employer.companyLogo().isEmpty()){
        cloudinaryUploadService.uploadFile(employer.companyLogo(), "companyLogo");
    }
    Employer saveEmployer = employerRepository.save(employerMapper.toEmployer(employer));
    return employerMapper.toEmployerResponse(saveEmployer);
    }


    @Override
    public EmployerResponse updateEmployer(EmployerRequest employer, Long id) {
        if(employer.email() == null || employer.companyName()==null){
            throw new CustomBadRequest("Enter data appropriately");
        }
        Employer existingEmployer = employerRepository.findById(id).orElseThrow(()
                -> new CustomNotFound("Job Seeker with id: "+id+ "does not exist"));

        if(employer.companyLogo() !=null && !employer.companyLogo().isEmpty()){
            cloudinaryUploadService.uploadFile(String.valueOf(employer.companyLogo()), "companyLogo");
        }

        Employer saveEmployer = employerRepository.save(existingEmployer);
        return employerMapper.toEmployerResponse(saveEmployer);
    }


    @Override
    public JobSeekerResponse findJobSeekerByEmail(String email) {
        //validate the role - not yet implemented
        if(email ==null) {
            throw new CustomBadRequest("Enter appropriate token");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) { throw new UserDoesNotExist("User is not authenticated yet");}
        JobSeeker jobSeeker = (JobSeeker) jobSeekerRepository.findJobSeekerByUser(user).orElseThrow( () -> new UserDoesNotExist("No user data found, add your data first"));
        return jobSeekerMapper.toJobSeekerResponse(jobSeeker);
    }


    @Override
    public EmployerResponse findEmployerByEmail(String email) {
        //validate the role - not yet implemented

        if (email == null) { throw new CustomBadRequest("Enter appropriate token");}
        User user = userRepository.findByEmail(email);
        if(user == null) throw new UserDoesNotExist("User is not authenticated yet");
        Employer employer = employerRepository.findByUser(user).orElseThrow(()-> new UserDoesNotExist("No user data found, add your data first"));
        return employerMapper.toEmployerResponse(employer);
    }
}