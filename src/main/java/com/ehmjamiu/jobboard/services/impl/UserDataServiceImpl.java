package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.requests.EmployerDto;
import com.ehmjamiu.jobboard.dtos.requests.EmployerDto2;
import com.ehmjamiu.jobboard.dtos.requests.EmployerRequest;
import com.ehmjamiu.jobboard.dtos.requests.JobSeekerRequest;
import com.ehmjamiu.jobboard.dtos.responses.*;
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

import java.util.ArrayList;
import java.util.List;
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
            cloudinaryUploadService.uploadFile(jobSeeker.resumeFile(), "resumeFile");
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
            cloudinaryUploadService.uploadFile(jobSeeker.resumeFile(), "resumeFile");
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
            cloudinaryUploadService.uploadFile(employer.companyLogo(), "companyLogo");
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

    @Override
    public AllUserResponse getAllUserData() {
        //validate the role - not yet implemented
        //roleUtils.validateSingleRole(UserRole.ADMIN);
        List<Employer> employerList = employerRepository.findAll();
        List<JobSeeker> jobSeekerList = jobSeekerRepository.findAll();
        List<Object> allUserTogether = new ArrayList<>();
        employerList.forEach(employer -> allUserTogether.add(
                new EmployerResponse(employer.getId(), employer.getCompanyName(), employer.getCompanyDescription(),
                                    employer.getLogoUrl(), employer.getWebsiteUrl(), employer.getUser().getEmail())));
        jobSeekerList.forEach(jobSeeker -> allUserTogether.add(
                new JobSeekerResponse(jobSeeker.getId(), jobSeeker.getUser().getEmail(),jobSeeker.getExperience(),
                        jobSeeker.getFirstName(),jobSeeker.getLastName(),jobSeeker.getResumeUrl(),jobSeeker.getSkills())));

        return new AllUserResponse(allUserTogether);
    }

    @Override
    public AllUserResponse getAllUser() {
        //validate the role - not yet implemented
        //roleUtils.validateSingleRole(UserRole.ADMIN);
        List<User> users = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        users.forEach(user -> userDtoList.add(
                new UserDto(user.getId(),user.getEmail(),user.getRole(),user.getCreatedAt(),user.getUpdatedAt())));

        return new AllUserResponse(userDtoList);
    }

    @Override
    public AllUserResponse getAllJobSeeker() {
        //validate the role - not yet implemented
        //roleUtils.validateSingleRole(UserRole.ADMIN);
        List<JobSeeker> jobSeekers = jobSeekerRepository.findAll();
        List<JobSeekerDto> jobSeekerDtoList = new ArrayList<>();
        jobSeekers.forEach(jobSeeker -> jobSeekerDtoList.add(
                new JobSeekerDto(jobSeeker.getId(), jobSeeker.getFirstName(), jobSeeker.getLastName(),
                        jobSeeker.getEmail(),  jobSeeker.getUser().getRole(),
                        jobSeeker.getResumeUrl(), jobSeeker.getSkills(), jobSeeker.getExperience())));

        return new AllUserResponse(jobSeekerDtoList);
    }

    @Override
    public AllUserResponse getAllEmployer() {
        //validate the role - not yet implemented
        //roleUtils.validateSingleRole(UserRole.ADMIN);
        List<Employer> jobSeekers = employerRepository.findAll();
        List<EmployerDto2> employerDtoList = new ArrayList<>();
        jobSeekers.forEach(employer -> employerDtoList.add(
                new EmployerDto2(employer.getUser().getEmail(), employer.getCompanyName(), employer.getCompanyDescription(),
                        employer.getLogoUrl(), employer.getWebsiteUrl(), employer.getUser().getId())));

        return new AllUserResponse(employerDtoList);
    }

    @Override
    public void deleteJobSeekerById(Long id) {
        //validate the role - not yet implemented
        //roleUtils.validateSingleRole(UserRole.ADMIN);
        if (id==null || !jobSeekerRepository.existsById(id)){
            throw new CustomBadRequest("Job Seeker with this id: " + id + " does not exist");
        }
        Optional<JobSeeker> jobSeeker = jobSeekerRepository.findById(id);
        if(jobSeeker.isPresent()){
            JobSeeker seeker = jobSeeker.get();
            if (seeker.getResumeUrl()!=null && !seeker.getResumeUrl().isEmpty()){
                cloudinaryUploadService.deleteFile(seeker.getResumeUrl());
            }
        }
        employerRepository.deleteById(id);
    }



    @Override
    public void deleteEmployerById(Long id) {
        //validate the role - not yet implemented
        //roleUtils.validateSingleRole(UserRole.ADMIN);

        if (id==null || !jobSeekerRepository.existsById(id)){
            throw new CustomBadRequest("Employer with this id: " + id + " does not exist");
        }
        Optional<Employer> employer = employerRepository.findById(id);
        if(employer.isPresent()){
            Employer emp = employer.get();
            if (emp.getLogoUrl()!=null && !emp.getLogoUrl().isEmpty()){
                cloudinaryUploadService.deleteFile(emp.getLogoUrl());
            }
        }
        employerRepository.deleteById(id);
    }
}