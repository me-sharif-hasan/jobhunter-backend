package com.iishanto.jobhunterbackend.domain.service;

import com.iishanto.jobhunterbackend.domain.adapter.JobParserAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;
import com.iishanto.jobhunterbackend.domain.usecase.IndexJobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobAggregatorService implements IndexJobs {
    private JobParserAdapter jobParserAdapter;
    public JobAggregatorService(@Autowired JobParserAdapter jobParserAdapter){
        this.jobParserAdapter=jobParserAdapter;
    }
    public List<SimpleJobModel> getJobFromUrl(String url){
        return jobParserAdapter.parse(url);
    }

    @Override
    public void index() {

    }
}
