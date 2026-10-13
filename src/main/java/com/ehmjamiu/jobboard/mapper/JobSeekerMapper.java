package com.ehmjamiu.jobboard.mapper;

import com.ehmjamiu.jobboard.dtos.requests.JobSeekerRequest;
import com.ehmjamiu.jobboard.dtos.responses.JobSeekerResponse;
import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.models.User;

public class JobSeekerMapper {

    public JobSeeker toJobSeeker (JobSeekerRequest jobSeekerRequest){
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setEmail(jobSeekerRequest.email());
        jobSeeker.setFirstName(jobSeekerRequest.firstName());
        jobSeeker.setLastName(jobSeekerRequest.lastName());
        jobSeeker.setSkills(jobSeekerRequest.skill());
        jobSeeker.setExperience(jobSeekerRequest.experience());

        User user = new User();

        user.setId(jobSeekerRequest.userId());

        jobSeeker.setUser(user);

        return jobSeeker;
    }


    public JobSeekerResponse toJobSeekerResponse (JobSeeker jobSeeker) {
        return new JobSeekerResponse(
                jobSeeker.getId(), jobSeeker.getEmail(), jobSeeker.getFirstName(),
                jobSeeker.getLastName(), jobSeeker.getResumeUrl(), jobSeeker.getSkills(), jobSeeker.getExperience()

        );
    }


}