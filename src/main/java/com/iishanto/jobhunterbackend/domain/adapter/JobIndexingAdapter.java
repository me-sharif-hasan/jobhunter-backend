package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;

import java.util.List;

public interface JobIndexingAdapter {
    public void refreshIndexingQueue();
    public void indexJob(OnIndexingDone onIndexingDone);
    Jobs getJobMetadata(Jobs job);

    interface OnIndexingDone{
        void onIndexingDone(List<String> jobIds);
    }
}
