package com.ehmjamiu.jobboard.repositories;


import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.models.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    @Query("""
           SELECT savedJob FROM SavedJob savedJob WHERE savedJob.jobSeeker =:jobSeeker""")
    List<SavedJob> findAllByJobSeeker(@Param("jobSeeker") JobSeeker jobSeeker);


    Optional<SavedJob> findSavedJobByIdAndJobSeeker(Long id, JobSeeker jobSeeker);

    boolean existsByJobAndJobSeeker(Job job, JobSeeker jobSeeker);
}