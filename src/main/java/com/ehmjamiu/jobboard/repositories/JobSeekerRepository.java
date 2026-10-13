package com.ehmjamiu.jobboard.repositories;

import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {
    Optional<JobSeeker> findJobSeekerByEmail(String email);

    List<JobSeeker> id(Long id);

    Optional<Object> findJobSeekerById(Long id);

    Optional<Object> findJobSeekerByUser(User user);
}
