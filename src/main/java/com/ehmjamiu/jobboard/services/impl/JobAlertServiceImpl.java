package com.ehmjamiu.jobboard.services.impl;


import com.ehmjamiu.jobboard.dtos.requests.JobAlertRequest;
import com.ehmjamiu.jobboard.exceptions.CustomBadRequest;
import com.ehmjamiu.jobboard.exceptions.CustomNotFound;
import com.ehmjamiu.jobboard.models.Job;
import com.ehmjamiu.jobboard.models.JobAlert;
import com.ehmjamiu.jobboard.models.JobSeeker;
import com.ehmjamiu.jobboard.models.enums.AlertFrequency;
import com.ehmjamiu.jobboard.repositories.JobAlertRepository;
import com.ehmjamiu.jobboard.repositories.JobRepository;
import com.ehmjamiu.jobboard.repositories.JobSeekerRepository;
import com.ehmjamiu.jobboard.services.EmailService;
import com.ehmjamiu.jobboard.services.JobAlertService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobAlertServiceImpl implements JobAlertService {

    private final JobAlertRepository jobAlertRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;


    public JobAlertServiceImpl(JobAlertRepository jobAlertRepository, JobSeekerRepository jobSeekerRepository, JobRepository jobRepository, EmailService emailService) {
        this.jobAlertRepository = jobAlertRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }


    @Override
    public void setJobAlert(JobAlertRequest alert, String email) {
        if (alert.searchedQuery()==null||alert.searchedQuery().isEmpty()){
            throw new CustomBadRequest("Searched query is empty");
        }

        JobSeeker jobSeeker = jobSeekerRepository.findJobSeekerByEmail(email).orElseThrow(()
                -> new CustomNotFound("Could not find JobSeeker, please complete your job seeker details"));
        AlertFrequency frequency;
        if (alert.frequency()==null){
            frequency=AlertFrequency.DAILY;
        }else {
            frequency=alert.frequency();
        }
        if (jobAlertRepository.existsByJobSeekerId(jobSeeker.getId())) {
            JobAlert existingJobAlert = jobAlertRepository.findJobAlertByJobSeekerId(jobSeeker.getId());
            existingJobAlert.setFrequency(frequency);
            existingJobAlert.setSearchedQuery(alert.searchedQuery());
            jobAlertRepository.saveAndFlush(existingJobAlert);
        }else{
            JobAlert jobAlert = new JobAlert();
            jobAlert.setSearchedQuery(alert.searchedQuery());
            jobAlert.setJobSeeker(jobSeeker);
            jobAlert.setFrequency(frequency);
            jobAlertRepository.save(jobAlert);
        }

    }

    @Override
    public void deleteJobAlert(Long jobAlertId, String email) {
        if (jobAlertId == null) {
            throw new CustomBadRequest("Job alert id is null");
        }
        if(email==null||email.isEmpty()){
            throw new CustomBadRequest("Email is empty");
        }
        jobAlertRepository.findJobAlertById(jobAlertId).orElseThrow(()
                -> new CustomNotFound("You've not set any job alerts"));
        jobAlertRepository.deleteJobAlert(jobAlertId);
    }

    private void sendJobAlert(List<JobAlert> jobAlerts, String subject) {
        for (JobAlert jobAlert : jobAlerts) {
            List<Job> availableJobs = jobRepository.searchJob(jobAlert.getSearchedQuery());
            if(!availableJobs.isEmpty()){
                emailService.sendJobAlertsEmail(jobAlert.getJobSeeker().getUser().getEmail(),
                        subject,jobAlert.getJobSeeker().getFirstName(),
                        availableJobs,
                        jobAlert.getSearchedQuery(),
                        "http://localhost:8081/api/v1/unsubscribe/"
                                +jobAlert.getId()+"/"
                                +jobAlert.getJobSeeker().getUser().getEmail());
            }
        }
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailyJobAlerts() {
        List<JobAlert> jobAlerts = jobAlertRepository.findAllByFrequency((AlertFrequency.DAILY);
        sendJobAlert(jobAlerts,"Daily Job Alerts");

    }

    @Scheduled(cron = "0 0 8 * * MON")
    public void sendWeeklyJobAlerts() {
        List<JobAlert> jobAlerts = jobAlertRepository.findAllByFrequency(AlertFrequency.WEEKLY);
        sendJobAlert(jobAlerts,"Weekly Job Alerts");
    }

}