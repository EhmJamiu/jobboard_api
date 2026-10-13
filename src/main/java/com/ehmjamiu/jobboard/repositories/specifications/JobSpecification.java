package com.ehmjamiu.jobboard.repositories.specifications;

import com.ehmjamiu.jobboard.dtos.requests.*;
import com.ehmjamiu.jobboard.models.Employer;
import com.ehmjamiu.jobboard.models.Job;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class JobSpecification {
    public static Specification<Job> advancedFilter (AdvancedFilterRequest filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Job, Employer> jobEmployerJoin = root.join("employer");

            if(filter.title() != null && !filter.title().trim().isEmpty()){
                predicates.add(builder.like(builder.lower(root.get("title")), "%" +filter.title().toLowerCase()+ "%"));
            }
            if(filter.description() != null && !filter.description().trim().isEmpty()){
                predicates.add(builder.like(builder.lower(root.get("description")), "%" + filter.description().toLowerCase()+"%"));
            }

            if(filter.category() != null && !filter.category().trim().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("category")), "%"+ filter.category().toLowerCase()+"%"));
            }
            if(filter.location() != null && !filter.location().trim().isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("location")), "%"+ filter.location().toLowerCase()+"%"));
            }

            if(filter.maxSalary() != null && filter.maxSalary() >= 0) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("maxSalary"), filter.maxSalary()));
            }

            if(filter.minSalary() != null && filter.minSalary() >= 0) {
                predicates.add(builder.lessThanOrEqualTo(root.get("maxSalary"), filter.minSalary()));
            }

            if(filter.startAt() != null) {
                predicates.add(builder.equal(root.get("postedAt"), filter.startAt()));
            }

            if(filter.endAt() != null) {
                predicates.add(builder.equal(root.get("postedAt"), filter.endAt()));
            }

            predicates.add(builder.or(builder.isNull(root.get("expiresAt")), builder.greaterThan(root.get("expiresAt"), LocalDateTime.now())));

            return builder.and(predicates.toArray(new Predicate[0]));

        } ;



    }

}