package com.ehmjamiu.jobboard.repositories;

import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.JobAlert;
import com.ehmjamiu.jobboard.models.enums.AlertFrequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobAlertRepository extends JpaRepository<JobAlert, Long> {


    //JobAlert findByJobSeekerId(Long id);

    JobAlert findJobAlertByJobSeeker(Long id);

    @Query("""
            SELECT j FROM JobAlert  j WHERE j.id=:id""")
    Optional<JobAlert> findJobAlertById(@Param("id")Long jobAlertId);

    @Modifying
    @Query("""
            DELETE FROM JobAlert j WHERE j.id =:id""")
    void deleteJobAlert(@Param("id")Long id);


    @Query("""
        SELECT j FROM JobAlert j WHERE j.jobSeeker.id=:id""")
    JobAlert findJobAlertByJobSeekerId(Long id);


    boolean existsByJobSeekerId(Long id);

    List<JobAlert> findAllByFrequency(AlertFrequency frequency);
}