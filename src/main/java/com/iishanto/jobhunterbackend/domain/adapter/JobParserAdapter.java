package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;

import java.util.List;

public interface JobParserAdapter {
    public List<SimpleJobModel> parse(String url);
}
