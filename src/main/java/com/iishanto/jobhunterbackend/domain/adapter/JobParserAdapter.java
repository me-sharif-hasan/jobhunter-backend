package com.iishanto.jobhunterbackend.domain.adapter;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobModel;

import java.util.List;

public interface JobParserAdapter {
    List<SimpleJobModel> parse(String url);
}
