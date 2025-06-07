package com.iishanto.jobhunterbackend.domain.usecase;

import com.iishanto.jobhunterbackend.domain.model.SimpleJobCommentModel;

import java.util.List;

public interface JobCommentUseCase {
    String addJobComment(SimpleJobCommentModel model);
    List<SimpleJobCommentModel> getAllJobComments(String jobId, int limit, Long startAt);
}
