package com.ehmjamiu.jobboard.services.impl;

import com.ehmjamiu.jobboard.dtos.responses.SavedJobResponse;
import com.ehmjamiu.jobboard.exceptions.CustomBadRequest;
import com.ehmjamiu.jobboard.exceptions.CustomNotFound;
import com.ehmjamiu.jobboard.mapper.SavedJobMapper;
import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.models.SavedJob;
import com.ehmjamiu.jobboard.repositories.JobRepository;
import com.ehmjamiu.jobboard.repositories.JobSeekerRepository;
import com.ehmjamiu.jobboard.repositories.SavedJobRepository;
import com.ehmjamiu.jobboard.services.SavedJobService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SavedJobServiceImpl implements SavedJobService {

    private final JobSeekerRepository jobSeekerRepository;
    private final SavedJobRepository savedJobRepository;
    private final SavedJobMapper savedJobMapper;
    private final JobRepository jobRepository;



    public SavedJobServiceImpl(JobSeekerRepository jobSeekerRepository, SavedJobRepository savedJobRepository, SavedJobMapper savedJobMapper, JobRepository jobRepository) {
        this.jobSeekerRepository = jobSeekerRepository;
        this.savedJobRepository = savedJobRepository;
        this.savedJobMapper = savedJobMapper;
        this.jobRepository = jobRepository;
    }


    @Override
    public SavedJobResponse getSavedJobById(Long id, String email) {
        if(id==null || email==null){
            throw new CustomBadRequest("Saved id or seekerEmail is null");
        }
        JobSeeker jobSeekers = jobSeekerRepository.findJobSeekerByEmail(email).orElseThrow(()->new CustomNotFound("Complete your job seeker details"));
        SavedJob s = savedJobRepository.findSavedJobByIdAndJobSeeker(id, jobSeekers).orElseThrow(()->new CustomNotFound("Job not found"));
        return savedJobMapper.toSavedJobResponse(s);
    }

    @Override
    public Stream<SavedJobResponse> getAllSavedJobByJobSeeker(String email) {
        if(email==null){
            throw new CustomBadRequest("Job seekerEmail is null");
        }
        JobSeeker jobSeeker = jobSeekerRepository.findJobSeekerByEmail(email).orElseThrow(()
                -> new CustomNotFound("Complete your job seeker details"));
        List<SavedJob> savedJob = savedJobRepository.findAllByJobSeeker(jobSeeker);
        return savedJob.stream().map(savedJobMapper::toSavedJobResponse);
    }
//existsByJobsAndJobSeekers
    @Override
    public void saveJob(Long id, String email) {
        if(id==null || email==null){
            throw new CustomBadRequest("Job id or seekerEmail is null");
        }
        Job job = jobRepository.findById(id).orElseThrow(()->new CustomNotFound("Job not found"));
        JobSeeker jobSeeker = jobSeekerRepository.findJobSeekerByEmail(email).orElseThrow(()->new CustomNotFound("Complete your job seeker details"));
        if(savedJobRepository.existsByJobAndJobSeeker(job, jobSeeker)){
            throw new CustomBadRequest("You cannot save a job that already exists");
        }

        LocalDateTime savedAt = LocalDateTime.now();
        SavedJob savedJob = new SavedJob();
        savedJob.setSavedAt(savedAt);
        savedJob.setJobSeeker(jobSeeker);
        savedJob.setJob(job);

        savedJobRepository.save(savedJob);

    }

    @Override
    public void deleteJob(Long savedJobId, String email) {
        if(savedJobId==null || email==null){
            throw new CustomBadRequest("Job id or seekerEmail is null");
        }
        JobSeeker jobSeekers = jobSeekerRepository.findJobSeekerByEmail(email).orElseThrow(()
                -> new CustomNotFound("Complete your job seeker details"));
        savedJobRepository.findById(savedJobId).orElseThrow(()
                -> new CustomNotFound("Job not found"));
        SavedJob savedJob = savedJobRepository.findSavedJobByIdAndJobSeeker(savedJobId,jobSeekers).orElseThrow(()
                -> new CustomNotFound("Either this Job not saved or you can't delete it if you are the not the one who saved it"));
        savedJobRepository.delete(savedJob);

    }
}