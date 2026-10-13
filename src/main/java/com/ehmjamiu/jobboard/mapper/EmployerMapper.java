package com.ehmjamiu.jobboard.mapper;

import com.ehmjamiu.jobboard.dtos.requests.EmployerDto;
import com.ehmjamiu.jobboard.dtos.responses.EmployerResponse;
import com.ehmjamiu.jobboard.models.Employer;
import com.ehmjamiu.jobboard.models.User;

public class EmployerMapper {

    public Employer toEmployer(EmployerDto request) {
        Employer employer = new Employer();
        employer.setCompanyName(request.companyName());
        employer.setCompanyDescription(request.companyDescription());
        employer.setLogoUrl(request.companyLogo());
        employer.setWebsiteUrl(request.websiteUrl());

        User user = new User();
        user.setId(employer.getId());
        employer.setUser(user);
        return employer;

    }

    public EmployerResponse toEmployerResponse(Employer employer){
        return new EmployerResponse(
                employer.getId(),
                employer.getCompanyName(),
                employer.getCompanyDescription(),
                employer.getLogoUrl(),
                employer.getWebsiteUrl(),
                employer.getUser().getEmail()
        );
    }
}