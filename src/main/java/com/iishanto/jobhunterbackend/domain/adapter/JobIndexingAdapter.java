package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Jobs;

import java.util.List;

public interface JobIndexingAdapter {
    public void refreshIndexingQueue();
    public void indexJob(OnIndexingDone onIndexingDone);
    Jobs getJobMetadata(Jobs job);
    Jobs getJobMetadata(Jobs job,String baseContext);
    List<SiteAttributeValidatorModel> getSiteAttributeValidatorModels(List<Long> siteIds);
    interface OnIndexingDone{
        void onIndexingDone(List<String> jobIds);
    }
}
