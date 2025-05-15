package com.iishanto.jobhunterbackend.infrastructure.tasks.duplicateutil;

import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AlgorithmicDuplicateJobMarkerImpl implements DuplicateJobMarker{
    JobsRepository jobsRepository;
    @Override
    public void findDuplicates() {

    }

    @Override
    public void markDuplicates() {

    }

    @Override
    public void findAndMarkDuplicates() {

    }
}
