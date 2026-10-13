package com.ehmjamiu.jobboard.repositories;

import com.ehmjamiu.jobboard.models.Employer;
import com.ehmjamiu.jobboard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Employer findEmployerByUser(User user);

    Optional<Employer> findByUser(User user);
}
