package com.ehmjamiu.jobboard.services;

import com.ehmjamiu.jobboard.dtos.responses.SavedJobResponse;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public interface SavedJobService {

    SavedJobResponse getSavedJobById(Long id, String email);
    Stream<SavedJobResponse> getAllSavedJobByJobSeeker(String email);
    void saveJob(Long id, String email);
    void deleteJob(Long savedJobId, String email);
}