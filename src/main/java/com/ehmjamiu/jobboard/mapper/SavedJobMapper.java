package com.ehmjamiu.jobboard.mapper;

import com.ehmjamiu.jobboard.dtos.requests.SavedJobDto;
import com.ehmjamiu.jobboard.dtos.responses.JobResponse;
import com.ehmjamiu.jobboard.dtos.responses.SavedJobResponse;
import com.ehmjamiu.jobboard.models.SavedJob;

public class SavedJobMapper {

    public SavedJobResponse toSavedJobResponse(SavedJob savedJob) {
        JobResponse jobData = new JobResponse(
                savedJob.getId(),
                savedJob.getJob().getEmployer().getCompanyName(),
                savedJob.getJob().getEmployer().getCompanyDescription(),
                savedJob.getJob().getEmployer().getWebsiteUrl(),
                savedJob.getJob().getTitle(),
                savedJob.getJob().getDescription(),
                savedJob.getJob().getLocation(),
                savedJob.getJob().getCategory(),
                savedJob.getJob().getStatus(),
                savedJob.getJob().getSalary(),
                savedJob.getJob().getPostedAt(),
                savedJob.getJob().getExpiredAt()
        );

        return new SavedJobResponse(
              savedJob.getId(),
              savedJob.getSavedAt(),
                jobData

        );
    }

    public SavedJob toSavedJob(SavedJobDto savedJobDto) {
        SavedJob savedJob = new SavedJob();
        savedJob.setJobSeeker(savedJobDto.jobSeeker());
        savedJob.setJob(savedJobDto.job());
        savedJob.setSavedAt(savedJobDto.savedAt());
        return savedJob;
    }

}