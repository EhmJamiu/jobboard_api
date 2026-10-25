package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.requests.*;
import com.ehmjamiu.jobboard.dtos.responses.JobResponse;
import com.ehmjamiu.jobboard.exceptions.*;
import com.ehmjamiu.jobboard.exceptions.CustomNotFound;
import com.ehmjamiu.jobboard.mapper.JobMapper;
import com.ehmjamiu.jobboard.models.Employer;
import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.User;
import com.ehmjamiu.jobboard.models.enums.UserRole;
import com.ehmjamiu.jobboard.repositories.EmployerRepository;
import com.ehmjamiu.jobboard.repositories.JobRepository;
import com.ehmjamiu.jobboard.repositories.UserRepository;
import com.ehmjamiu.jobboard.repositories.specifications.*;
import com.ehmjamiu.jobboard.services.JobService;
import com.ehmjamiu.jobboard.utils.RoleUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final RoleUtils roleUtils;

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final EmployerRepository employerRepository;

    public JobServiceImpl(JobMapper jobMapper, RoleUtils roleUtils, UserRepository userRepository, JobRepository jobRepository, EmployerRepository employerRepository) {
        this.jobMapper = jobMapper;
        this.roleUtils = roleUtils;
        this.userRepository = userRepository;

        this.jobRepository = jobRepository;
        this.employerRepository = employerRepository;
    }


    @Override
    @Cacheable(value = "allJobs", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<JobResponse> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable)
                .map(job -> jobMapper.toJobResponse(job));

    }

    @Override
    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() ->
                new CustomNotFound("Job with id: " + id + " is not found"));
        return jobMapper.toJobResponse(job);
    }

    @Override
    public Page<JobResponse> filterAllByJobTitle(String keyword, Pageable pageable) {
        return jobRepository.findAllByTitleContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public Page<JobResponse> filterAllByAdvanceSearch(String keyword, Pageable pageable) {
        return jobRepository.advanceSearch(keyword, pageable);
    }

    @Override
    public Page<JobResponse> filterJob(String title, String description, String location, String category, String companyName, Double minSalary,
                                          Double maxSalary, LocalDateTime startAt, LocalDateTime endAt, Pageable pageable) {
        boolean hasValidSearch = Stream.of(title, description, location, category)
                .anyMatch(string -> string != null && !string.trim().isEmpty())||
                        minSalary !=null && minSalary >= 0 ||
                        maxSalary !=null && maxSalary >= 0 ||
                        startAt !=null || endAt !=null;
        if(!hasValidSearch) {
            throw new CustomBadRequest("At least one valid search parameter is required (job title, description, company name, category, salary range, or date range)");
        }

        AdvancedFilterRequest filter = new AdvancedFilterRequest(title, description, companyName, location, category, minSalary, maxSalary, startAt, endAt);
        Specification<Job> spec = JobSpecification.advancedFilter(filter);
        Page<JobResponse> jobs = jobRepository.findAll(spec, pageable).map(job -> jobMapper.toJobResponse(job));
        return jobs;

    }

    @Override
    public void postJob(JobRequest request, String email) {
        //validate the employer role
        roleUtils.validateSingleRole(UserRole.EMPLOYER);

        if(request.description() ==null || !request.title().isEmpty() || request.title() == null || request.description().isEmpty()){
            throw new CustomBadRequest("Job title and description is/are required");
        }
        if(request.expiredAt().isBefore(LocalDateTime.now())){
            throw new CustomBadRequest("Job expiring time should beyond the current time");
        }
        User user = userRepository.findByEmail(email);
        if(user == null) throw new UserDoesNotExist("User is not authenticated");
        Employer employer = employerRepository.findByUser(user).orElseThrow(()->
                new UserDoesNotExist("Update your employer's data"));

        //seems this declaration is not applied to the job posted status
        // Ohh!!! record; it is immutable
        //request.status() = request.expiredAt().isAfter(LocalDateTime.now())?JobStatus.ACTIVE:JobStatus.EXPIRED;
//        JobStatus jobStatus;
//        jobStatus=JobStatus.ACTIVE;
//        if(request.expiredAt().isBefore(LocalDateTime.now())) {
//            jobStatus = JobStatus.EXPIRED;
//        }

        Job savedJob = jobMapper.toJob(request);
        jobRepository.save(savedJob);
    }


    @Override
    public void updateJob(JobRequest request, Long id) {
        if(request.description() ==null || !request.title().isEmpty() ||request.description().isEmpty()){
            throw new CustomBadRequest("Job title and description is/are required");
        }
        if(request.expiredAt().isBefore(LocalDateTime.now())){
            throw new CustomBadRequest("Job expiring time should beyond the current time");
        }
        Job existingJob = jobRepository.findById(id).orElseThrow(()->new CustomNotFound("This job does not exist"));
        jobRepository.save(existingJob);
    }

    @Override
    public void deleteJob(Long id, String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) throw new UserDoesNotExist("This user does not exist");
        Employer employer = employerRepository.findByUser(user).orElseThrow(()
                ->new UserDoesNotExist("This employer does not exist"));

        roleUtils.validateAnyRoles(UserRole.ADMIN,employer.getUser().getRole());

        Job jobs = jobRepository.findById(id).orElseThrow(()->new CustomNotFound("This job does not exist"));
        jobRepository.deleteById(jobs.getId());
    }


}