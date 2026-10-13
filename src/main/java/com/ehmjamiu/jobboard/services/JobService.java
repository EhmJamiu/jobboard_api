package com.ehmjamiu.jobboard.services;

import com.ehmjamiu.jobboard.dtos.responses.JobResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface JobService {

    Page<JobResponse> getAllJobs(Pageable pageable);

    JobResponse getJobById(Long id);

    Page<JobResponse> filterAllByJobTitle(String keyword, Pageable pageable);

    Page<JobResponse> filterAllByAdvanceSearch(String keyword, Pageable pageable);
    Page<JobResponse> filterJob(String title, String description, String location, String category, String companyName, Double minSalary,
            Double maxSalary, LocalDateTime startAt, LocalDateTime endAt, Pageable pageable);
}