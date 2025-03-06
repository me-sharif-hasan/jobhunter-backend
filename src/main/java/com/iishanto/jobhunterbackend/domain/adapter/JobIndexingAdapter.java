package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;

public interface JobIndexingAdapter {
    public void refreshIndexingQueue();
    public void indexJob();
    Jobs getJobMetadata(Jobs job);
}
