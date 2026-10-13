package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.requests.*;
import com.ehmjamiu.jobboard.dtos.responses.JobResponse;
import com.ehmjamiu.jobboard.exceptions.*;
import com.ehmjamiu.jobboard.exceptions.CustomNotFound;
import com.ehmjamiu.jobboard.mapper.JobMapper;
import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.repositories.JobRepository;
import com.ehmjamiu.jobboard.repositories.specifications.*;
import com.ehmjamiu.jobboard.services.JobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;

    private final JobRepository jobRepository;

    public JobServiceImpl(JobMapper jobMapper,  JobRepository jobRepository) {
        this.jobMapper = jobMapper;

        this.jobRepository = jobRepository;
    }


    @Override
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


}