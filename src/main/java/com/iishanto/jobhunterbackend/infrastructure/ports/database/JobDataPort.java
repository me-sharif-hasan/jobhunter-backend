package com.iishanto.jobhunterbackend.infrastructure.ports.database;

import com.iishanto.jobhunterbackend.domain.adapter.JobDataAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.admin.AdminJobDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobCommentModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserAppliedJobsModel;
import com.iishanto.jobhunterbackend.domain.model.values.JobApplicationStatus;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;
import com.iishanto.jobhunterbackend.infrastructure.database.User;
import com.iishanto.jobhunterbackend.infrastructure.database.UserAppliedJobs;
import com.iishanto.jobhunterbackend.infrastructure.database.firebase.JobComment;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobCommentRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserAppliedJobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JobDataPort implements AdminJobDataAdapter, JobDataAdapter {
    JobsRepository jobsRepository;
    UserRepository userRepository;
    JobCommentRepository jobCommentRepository;
    UserAppliedJobsRepository userAppliedJobsRepository;
    @Override
    public List<SimpleJobModel> getAllJobsForAdmin(int page, int limit, String query) {
        Pageable pageable = PageRequest.of(page, limit);
        List<Jobs> jobs = jobsRepository.findAllByJobDescriptionContainingOrTitleContainingOrLocationContainingOrderByJobParsedAtDesc(query,query,query,pageable);
        return jobs.stream().map(Jobs::toSimpleJobModel).collect(Collectors.toList());
    }

    @Override
    public long getTotalJobsCount() {
        return jobsRepository.count();
    }

    @Override
    public void updateJobDuplicateStatus(String jobId, boolean isDuplicate) {
        Jobs job = jobsRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        job.setDuplicate(isDuplicate);
        jobsRepository.save(job);
    }

    @Override
    public void updateJob(SimpleJobModel simpleJobModel) {
        Optional <Jobs> optionalJob = jobsRepository.findById(simpleJobModel.getJobId());
        if (optionalJob.isPresent()) {
            Jobs job = optionalJob.get();
            job.setJobType(simpleJobModel.getJobType());
            job.setSalary(simpleJobModel.getSalary());
            job.setLocation(simpleJobModel.getLocation());
            job.setJobUrl(simpleJobModel.getJobUrl());
            job.setTitle(simpleJobModel.getTitle());
            job.setJobCategory(simpleJobModel.getJobCategory());
            job.setJobDescription(simpleJobModel.getJobDescription());
            job.setJobPostedDate(simpleJobModel.getJobPostedDate());
            job.setJobLastDate(simpleJobModel.getJobLastDate());
            job.setJobApplyLink(simpleJobModel.getJobApplyLink());
            job.setJobApplyEmail(simpleJobModel.getJobApplyEmail());
            jobsRepository.save(job);
        } else {
            throw new RuntimeException("Job not found");
        }
    }

    @Override
    public void markApplied(String jobId, Long userId) {
        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Jobs job = jobsRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
            Optional<UserAppliedJobs> optionalUserAppliedJobs = userAppliedJobsRepository.findByJob_JobIdAndUser_Id(jobId,userId);
            UserAppliedJobs userAppliedJobs = optionalUserAppliedJobs.orElseGet(UserAppliedJobs::new);
            userAppliedJobs.setJob(job);
            userAppliedJobs.setUser(user);
            userAppliedJobs.setAppliedAt(Timestamp.from(Instant.now()));
            userAppliedJobs.setApplied(true);
            userAppliedJobs.setApplicationStatus(JobApplicationStatus.APPLIED);
            userAppliedJobsRepository.save(userAppliedJobs);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Already Applied");
        }
    }

    @Override
    public void unmarkApplied(String jobId, Long id) {
        Optional<UserAppliedJobs> optionalUserAppliedJobs = userAppliedJobsRepository.findByJob_JobIdAndUser_Id(jobId,id);
        if (optionalUserAppliedJobs.isPresent()) {
            UserAppliedJobs userAppliedJobs = optionalUserAppliedJobs.get();
            userAppliedJobs.setApplied(false);
            userAppliedJobs.setAppliedAt(null);
            userAppliedJobsRepository.save(userAppliedJobs);
        }
    }

    @Override
    public List<SimpleUserAppliedJobsModel> getAppliedJobs(Long userId, int page, int limit, String query) {
        Pageable pageable = PageRequest.of(page, limit);
        List<UserAppliedJobs> userAppliedJobs = userAppliedJobsRepository.findAllByUser_IdAndJob_TitleContainingOrJob_JobIdContainingOrJob_LocationContainingOrJob_JobDescriptionContainingOrJob_Site_NameContainingOrJob_Site_HomepageContaining(
                userId,
                query,
                query,
                query,
                query,
                query,
                query,
                pageable
        );
        return userAppliedJobs.stream().map(UserAppliedJobs::toUserAppliedJobsModel).toList();
    }

    @Override
    public void updateJobApplicationStatus(String jobId, Long userId, JobApplicationStatus status) {
        userAppliedJobsRepository.findByJob_JobIdAndUser_Id(jobId,userId).ifPresentOrElse(
                userAppliedJobs -> {
                    userAppliedJobs.setApplicationStatus(status);
                    userAppliedJobsRepository.save(userAppliedJobs);
                },
                () -> {
                    throw new RuntimeException("Job application not found for the user");
                }
        );
    }

    @Override
    public SimpleJobCommentModel postComment(SimpleJobCommentModel model) {
        return jobCommentRepository.set(JobComment.fromSimpleJobCommentModel(model)).toSimpleJobCommentModel();
    }

    @Override
    public List<SimpleJobCommentModel> getJobComments(String jobId, int limit, Long startAt) {
        if(startAt == null){
            startAt = -1L;
        }
        return jobCommentRepository.findByJobId(jobId,startAt,limit).stream().map(JobComment::toSimpleJobCommentModel).collect(Collectors.toList());
    }
}
