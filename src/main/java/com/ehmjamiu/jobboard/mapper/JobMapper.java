package com.ehmjamiu.jobboard.mapper;

import com.ehmjamiu.jobboard.dtos.requests.JobRequest;
import com.ehmjamiu.jobboard.dtos.responses.JobResponse;
import com.ehmjamiu.jobboard.models.Employer;
import com.ehmjamiu.jobboard.models.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public Job toJob(JobRequest jobRequest) {
        Job job = new Job();
        job.setTitle(jobRequest.title());
        job.setDescription(jobRequest.description());
        job.setLocation(jobRequest.location());
        job.setCategory(jobRequest.category());
        job.setStatus(jobRequest.status());
        job.setPostedAt(jobRequest.postedAt());
        job.setExpiredAt(jobRequest.expiredAt());

        Employer employer = new Employer();
        employer.setId(jobRequest.employerId());

        job.setEmployer(employer);


        return job;

    }

    public JobResponse toJobResponse(Job job){
        return new JobResponse(
            job.getId(), job.getEmployer().getCompanyName(),
                job.getEmployer().getCompanyDescription(), job.getEmployer().getWebsiteUrl(),
                job.getTitle(), job.getDescription(), job.getLocation(), job.getCategory(),
                job.getStatus(), job.getSalary(), job.getPostedAt(), job.getExpiredAt()

        );
    }

}