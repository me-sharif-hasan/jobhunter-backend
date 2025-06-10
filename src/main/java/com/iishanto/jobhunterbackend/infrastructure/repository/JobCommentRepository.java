package com.iishanto.jobhunterbackend.infrastructure.repository;

import com.github.fabiomaffioletti.firebase.repository.DefaultFirebaseRealtimeDatabaseRepository;
import com.github.fabiomaffioletti.firebase.repository.Filter;
import com.google.firebase.database.*;
import com.iishanto.jobhunterbackend.infrastructure.database.firebase.JobComment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Repository
@AllArgsConstructor
public class JobCommentRepository extends DefaultFirebaseRealtimeDatabaseRepository<JobComment,String> {
    public JobComment set(JobComment document) {
        document.setCreateAt(Timestamp.from(Instant.now()));
        document.setUpdateAt(Timestamp.from(Instant.now()));
        document.setUuid(UUID.randomUUID().toString());
        return super.set(document, document.getJobId());
    }

    public List<JobComment> findByJobId(String jobId, Long startAt, int limit) {
        return super.find(Filter.FilterBuilder.builder().orderBy("createAt").startAt(startAt+1).limitToFirst(limit).build(),jobId);
    }
}
