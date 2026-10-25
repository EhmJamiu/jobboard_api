package com.ehmjamiu.jobboard.services.impl;


import com.ehmjamiu.jobboard.dtos.requests.ApplicationRequest;
import com.ehmjamiu.jobboard.dtos.responses.JobResponse;
import com.ehmjamiu.jobboard.dtos.responses.applications.ApplicationResponse;
import com.ehmjamiu.jobboard.dtos.responses.applications.JobSeekerApplication;
import com.ehmjamiu.jobboard.exceptions.CustomBadRequest;
import com.ehmjamiu.jobboard.exceptions.CustomNotFound;
import com.ehmjamiu.jobboard.exceptions.UserDoesNotExist;

import com.ehmjamiu.jobboard.mapper.ApplicationMapper;
import com.ehmjamiu.jobboard.mapper.JobMapper;
import com.ehmjamiu.jobboard.models.Application;
import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.models.enums.ApplicationStatus;
import com.ehmjamiu.jobboard.models.enums.UserRole;
import com.ehmjamiu.jobboard.repositories.ApplicationRepository;
import com.ehmjamiu.jobboard.repositories.JobRepository;
import com.ehmjamiu.jobboard.repositories.JobSeekerRepository;
import com.ehmjamiu.jobboard.services.ApplicationService;
import com.ehmjamiu.jobboard.services.CloudinaryUploadService;
import com.ehmjamiu.jobboard.utils.RoleUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final RoleUtils roleUtils;

    private final JobRepository jobRepository;
    private final ApplicationMapper applicationMapper;
    private final JobSeekerRepository jobSeekerRepository;
    private final CloudinaryUploadService cloudinaryUploadService;

    private final JobMapper jobMapper;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, RoleUtils roleUtils, JobRepository jobRepository, ApplicationMapper applicationMapper, JobSeekerRepository jobSeekerRepository, CloudinaryUploadService cloudinaryUploadService, JobMapper jobMapper) {
        this.applicationRepository = applicationRepository;
        this.roleUtils = roleUtils;
        this.jobRepository = jobRepository;
        this.applicationMapper = applicationMapper;
        this.jobSeekerRepository = jobSeekerRepository;
        this.cloudinaryUploadService = cloudinaryUploadService;
        this.jobMapper = jobMapper;

    }

    @Override
    public void applyForJob(Long jobId, ApplicationRequest request, String email) {
        if(jobId==null) {
            throw new CustomBadRequest("Bad request");
        }
        // validate the role
        roleUtils.validateSingleRole(UserRole.JOB_SEEKER);
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new CustomBadRequest("Job not found"));
        JobSeeker jobSeeker = jobSeekerRepository.findJobSeekerByEmail(email).orElseThrow(() -> new CustomBadRequest("Either you are not authenticated or you need to update your profile"));
        if(applicationRepository.existsByJobAndJobSeeker(job, jobSeeker)){
            throw new CustomBadRequest("You can't apply for a job twice is already a job seeker");
        }

        String resumeUrl;
        if(request.resumeUrl()==null||!request.resumeUrl().isEmpty()) {
            if(jobSeeker.getResumeUrl()!=null) {
                resumeUrl = jobSeeker.getResumeUrl();
            }else {
                throw new CustomBadRequest("There must be a resume");
            }
        }else {
            resumeUrl =  cloudinaryUploadService.uploadFile(request.resumeUrl(), "application");
        }

        Application application = new Application();
        application.setJob(job);
        application.setJobSeeker(jobSeeker);
        application.setAppliedAt(LocalDateTime.now());
        application.setResumeUrl(resumeUrl);
        application.setCoverLetter(request.coverLetter());
        application.setStatus(ApplicationStatus.PENDING);
        applicationRepository.save(application);
    }

    @Override
    public void deleteApplication(Long applicationId, Long jobId, String email) {
        if(applicationId == null || jobId == null || email == null) {
            throw new CustomBadRequest("Bad request - required parameters missing");
        }
        JobSeeker jobSeekers = jobSeekerRepository.findJobSeekerByEmail(email).orElseThrow(()
                -> new UserDoesNotExist("Either you are not authenticated or you are yet to complete your profile before you can delete an application"));
        Application applications = applicationRepository.findApplicationByApplicationJobIdJobSeekerId(applicationId,jobId, jobSeekers.getId()).orElseThrow(()->new CustomNotFound("Application not found with id: "+applicationId + " for job id: "+jobId+" and job seeker id: "+ jobSeekers.getId()));
        applicationRepository.delete(applications);

    }

    @Override
    public List<JobSeekerApplication> getAllMyApplicationsForJobSeeker(String email) {

        roleUtils.validateSingleRole(UserRole.JOB_SEEKER);
        if(email==null) {
            throw new CustomBadRequest("Bad request");
        }
        JobSeeker jobSeeker = jobSeekerRepository.findJobSeekerByEmail(email).orElseThrow(()
                -> new UserDoesNotExist("Either you are not authenticated or you need to update your profile"));

        List<Application> myApplications = applicationRepository.findAllByJobSeeker(jobSeeker);
        return myApplications.stream().map(applications
                -> new JobSeekerApplication(applications.getResumeUrl(),
                applications.getCoverLetter(),
                new JobResponse(applications.getJob().getId(),
                        applications.getJob().getEmployer().getCompanyName(),
                        applications.getJob().getEmployer().getCompanyDescription(),
                        applications.getJob().getEmployer().getWebsiteUrl(),
                        applications.getJob().getTitle(),
                        applications.getJob().getDescription(),applications.getJob().getLocation(),
                        applications.getJob().getCategory(), applications.getJob().getStatus(),
                        applications.getJob().getSalary(),applications.getJob().getPostedAt(),
                        applications.getJob().getExpiredAt()))).toList();

    }

    @Override
    public List<ApplicationResponse> getAllApplicationsByEmployee(String email) {
        //validate the role
        roleUtils.validateSingleRole(UserRole.EMPLOYER);
        if(email==null) {
            throw new CustomBadRequest("Bad request");
        }
        List<Application> applications = applicationRepository.findAllApplicationByEmployerEmail(email);
        if(applications.isEmpty()){
            throw new CustomNotFound("No application found this company");
        }
        return applications.stream().map(applicationMapper::toApplicationResponse).toList();
    }

    @Override
    public List<ApplicationResponse> getAllApplicationsForJobIdByEmployee(String email, Long jobId) {
        if(email==null||jobId==null) {
            throw new CustomBadRequest("Bad request");
        }
        List<Application> jobIdApplications=applicationRepository.findAllApplicationByJobIdAndEmployerEmail(jobId,email);
        if (jobIdApplications.isEmpty()){
            throw new CustomBadRequest("No applications found for job "+jobId +" and company email "+email);
        }
        return jobIdApplications.stream().map(applicationMapper::toApplicationResponse).toList();
    }

    @Override
    public void updateApplicationStatus(Long applicationId, Long jobSeekerId, String email, ApplicationStatus status) {
        if (applicationId == null || email == null || jobSeekerId == null) {
            throw new CustomBadRequest("Bad request - required parameters missing");
        }
        try{
            switch (status) {
                case PENDING:
                case ACCEPTED:
                case REJECTED:
                case REVIEWED:
                    break;
                default:
                    throw new CustomBadRequest("Invalid application status: "+status);

            }
        } catch (IllegalArgumentException e) {
            throw new CustomBadRequest("Invalid application status: "+status);
        }
        Application application = applicationRepository.findApplicationsByApplicationJobSeekerIdEmployerEmail(applicationId,jobSeekerId,email).orElseThrow(()
                -> new CustomNotFound("Application not found with id: "+applicationId+" for jobSeeker id: "+jobSeekerId+" and employer email: "+email));
        application.setStatus(status);
        applicationRepository.save(application);
    }
}