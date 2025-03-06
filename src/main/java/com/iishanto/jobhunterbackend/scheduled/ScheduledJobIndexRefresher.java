package com.iishanto.jobhunterbackend.scheduled;

import com.iishanto.jobhunterbackend.domain.adapter.JobIndexingAdapter;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduledJobIndexRefresher {
    JobIndexingAdapter jobIndexingAdapter;
    @Scheduled(
            cron = "0 0 0 * * *"
//            fixedRate = 1000*60*60*24
    )
    public void refreshJobIndex(){
        jobIndexingAdapter.refreshIndexingQueue();
        jobIndexingAdapter.indexJob();
        System.out.println("Job Index Refreshed");
    }
}
