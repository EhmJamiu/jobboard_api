package com.ehmjamiu.jobboard.repositories;

import com.ehmjamiu.jobboard.models.Application;
import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByJobAndJobSeeker(Job job, JobSeeker jobSeeker);

    List<Application> findAllByJobSeeker(JobSeeker jobSeeker);

    @Query("""
            select applications from Application applications
            join fetch applications.job job
            join fetch job.employer employers
            join fetch employers.user user
            where user.email=:employerEmail""")
    List<Application> findAllApplicationByEmployerEmail(String email);

    @Query("""
        select application from Application application
        join fetch application.job job
        join fetch job.employer employer
        join fetch employer.user user
        where job.id=:jobId and user.email=:email""")

    List<Application> findAllApplicationByJobIdAndEmployerEmail(Long jobId, String email);
    @Query("""
        select application from Application application
        join fetch application.job job
        join fetch job.employer employer
        join fetch employer.user user
        where application.id=:applicationId
        and application.jobSeeker.id=:jobSeekerId
        and user.email=:employerEmail""")
    Optional<Application> findApplicationsByApplicationJobSeekerIdEmployerEmail(Long applicationId, Long jobSeekerId, String email);

    @Query("""
        select applications from Application applications
         join fetch applications.job job
         where applications.id=:applicationId 
         and job.id=:jobId and applications.jobSeeker.id=:jobSeekerId""")
    Optional<Object> findApplicationByApplicationJobIdJobSeekerId(Long applicationId, Long jobId, Long id);
}
