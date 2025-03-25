package com.iishanto.jobhunterbackend.scheduled;

import com.iishanto.jobhunterbackend.domain.adapter.JobIndexingAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.NotificationAdapter;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ScheduledJobIndexRefresher {
    JobIndexingAdapter jobIndexingAdapter;
    NotificationAdapter notificationAdapter;
    @Scheduled(
            cron = "0 0 0 * * *"
//            fixedRate = 1000*60*60*24
    )
    public void refreshJobIndex(){
        jobIndexingAdapter.refreshIndexingQueue();
        jobIndexingAdapter.indexJob(new JobIndexingAdapter.OnIndexingDone() {
            @Override
            public void onIndexingDone(List<String> jobIds) {
                System.out.println("Job Indexing Done");
                notificationAdapter.sendJobNotification(jobIds);
            }
        });
        System.out.println("Job Index Refreshed");
    }
}
