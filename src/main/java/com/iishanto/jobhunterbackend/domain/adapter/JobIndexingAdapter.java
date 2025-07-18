package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SiteAttributeValidatorModel;
import com.iishanto.jobhunterbackend.infrastructure.database.Opportunity;

import java.util.List;

public interface JobIndexingAdapter {
    public void refreshIndexingQueue();
    public void indexJob(OnIndexingDone onIndexingDone);
    Opportunity getJobMetadata(Opportunity job);
    Opportunity getJobMetadata(Opportunity job, String baseContext);
    List<SiteAttributeValidatorModel> getSiteAttributeValidatorModels(List<Long> siteIds);
    interface OnIndexingDone{
        void onIndexingDone(List<String> jobIds);
    }
}
