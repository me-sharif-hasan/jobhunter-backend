import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/domain/model/api_response.dart';
import 'package:personalized_job_hunter/features/common/domain/model/job_model.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

import '../../../../util/http/client.dart';

class JobDatasource{
  GetIt locator = GetIt.instance;
  BackendClient? client;
  JobDatasource() {
    client = locator<BackendClient>();
  }
  Future <List<Job>> getJobByLimit(int limit, currentPage, String searchQuery, int siteId,String variant) {
    return client!.get("${Constants.getJobs}?limit=$limit&page=$currentPage&query=$searchQuery&siteId=$siteId&variant=$variant").then((response) {
      if (response.statusCode == HttpStatus.ok) {
        log("--->${response.body}");
        final dynamic data = jsonDecode(utf8.decode(response.bodyBytes));
        ApiResponse apiResponse = ApiResponse.fromJson(data);
        log(data.toString());
        List<dynamic> jobs = apiResponse.data;
        List <Job> jobList = [];
        for (var job in jobs) {
          jobList.add(Job.fromJson(job));
        }
        return jobList;
      } else {
        throw Exception('Failed to load jobs');
      }
    });
  }

  Future<ApiResponse> markJobAsApplied(String jobId){
    return client!.get("${Constants.markJobAsApplied}?job_id=$jobId").then(
        (response){
          ApiResponse apiResponse = ApiResponse.fromJson(json.decode(utf8.decode(response.bodyBytes)));
          return apiResponse;
        }
    );
  }

  Future<ApiResponse> unmarkAJob(String jobId){
    return client!.get("${Constants.undoAppliedJob}?job_id=$jobId").then(
            (response){
          ApiResponse apiResponse = ApiResponse.fromJson(json.decode(utf8.decode(response.bodyBytes)));
          return apiResponse;
        }
    );
  }
}