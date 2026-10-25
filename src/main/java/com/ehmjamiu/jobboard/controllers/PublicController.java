package com.ehmjamiu.jobboard.controllers;

import com.ehmjamiu.jobboard.dtos.responses.JobResponse;
import com.ehmjamiu.jobboard.services.JobService;
import com.ehmjamiu.jobboard.utils.responseHandler.ResponseHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping("/api/jobs")
public class PublicController {
    private final JobService jobService;

    public PublicController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("")
    public ResponseEntity<Page<JobResponse>> getAllJobs(Pageable pageable) {
        return ResponseEntity.ok(jobService.getAllJobs(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/search/title")
    public ResponseEntity<Object> filterAllByJobTitle(@RequestParam String keyword, Pageable pageable) {
        //Page<JobResponse> response = jobService.filterAllByJobTitle(keyword, pageable);
        return ResponseEntity.ok(jobService.filterAllByJobTitle(keyword, pageable));
    }

    @GetMapping("/search/advance")
    public ResponseEntity<Object> searchJobByTitleOrDescriptionOrCategory(@RequestParam String keyword, Pageable pageable){
        Page<JobResponse> response = jobService.filterAllByAdvanceSearch(keyword, pageable);
        return ResponseHandler.handleResponse(response, HttpStatus.OK, "");
    }


    @GetMapping("/filter")
    public ResponseEntity<Object> filterSearch(@RequestParam(value = "title", required = false) String title,
                                               @RequestParam(value = "description", required = false) String description,
                                               @RequestParam(value = "location", required = false) String location,
                                               @RequestParam(value = "category", required = false) String category,
                                               @RequestParam(value = "companyName", required = false) String companyName,
                                               @RequestParam(value = "minSalary", required = false) Double minSalary,
                                               @RequestParam(value = "maxSalary", required = false) Double maxSalary,
                                               @RequestParam(value = "startAt", required = false) LocalDateTime startAt,
                                               @RequestParam(value = "endAt", required = false) LocalDateTime endAt,
                                               Pageable pageable){
        Page<JobResponse> response = jobService.filterJob(title, description, location, category, companyName, minSalary,
                                                            maxSalary, startAt, endAt, pageable);
        return ResponseHandler.handleResponse(response, HttpStatus.OK, "");
    }


}