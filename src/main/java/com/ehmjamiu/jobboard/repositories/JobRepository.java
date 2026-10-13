package com.ehmjamiu.jobboard.repositories;

import com.ehmjamiu.jobboard.dtos.responses.JobResponse;
import com.ehmjamiu.jobboard.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    @Query("""
            SELECT job FROM Job job WHERE job.title = :title""")
    Page<JobResponse> findAllByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    @Query("""
            SELECT job FROM Job job WHERE
                job.title ILIKE CONCAT('%', :keyword, '%') OR
                job.description ILIKE CONCAT('%', :keyword, '%') OR
                job.category ILIKE CONCAT('%', :keyword, '%')""")
    Page<JobResponse> advanceSearch(@Param("keyword") String keyword, Pageable pageable);
}
