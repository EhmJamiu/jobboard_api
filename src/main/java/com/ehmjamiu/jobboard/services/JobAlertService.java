package com.ehmjamiu.jobboard.services;


import com.ehmjamiu.jobboard.dtos.requests.JobAlertRequest;
import org.springframework.stereotype.Service;

@Service
public interface JobAlertService {
    void setJobAlert(JobAlertRequest alert, String email);
    void deleteJobAlert(Long jobAlertId, String email);
}
