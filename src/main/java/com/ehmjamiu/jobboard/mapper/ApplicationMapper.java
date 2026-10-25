package com.ehmjamiu.jobboard.mapper;

import com.ehmjamiu.jobboard.dtos.requests.ApplicationRequest;
import com.ehmjamiu.jobboard.dtos.responses.applications.JobSeekerApplied;
import com.ehmjamiu.jobboard.dtos.responses.applications.ApplicationResponse;
import com.ehmjamiu.jobboard.dtos.responses.applications.JobData;
import com.ehmjamiu.jobboard.models.Application;
import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.JobSeeker;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public void toApplication(ApplicationRequest request) {
        Application application = new Application();
        application.setCoverLetter(request.coverLetter());
        application.setResumeUrl(String.valueOf(request.resumeUrl()));

        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setId(request.jobSeeker().getId());

        Job job = new Job();
        job.setId(request.job().getId());

        application.setJobSeeker(jobSeeker);
        application.setJob(job);

    }

    public ApplicationResponse toApplicationResponse(Application application){

        return new ApplicationResponse(
            application.getResumeUrl(),
                application.getCoverLetter(), application.getAppliedAt(),
                application.getStatus(),
                application.getJobSeeker(),
                new JobData(application.getJob().getEmployer().getCompanyName(),
                        application.getJob().getEmployer().getWebsiteUrl(),
                        application.getJob().getStatus(),
                        application.getJob().getPostedAt(),
                        application.getJob().getExpiredAt(),
                        application.getJob().getSalary(),
                        application.getJob().getCategory()
                        )

        );
    }

}