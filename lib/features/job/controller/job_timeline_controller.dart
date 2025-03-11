import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/domain/model/job_model.dart';

import '../domain/datasource/job_datasource.dart';

class JobTimelineController extends ChangeNotifier{
  final List<Job> _jobs = [
  ];

  JobDatasource? _jobDatasource;
  GetIt locator = GetIt.instance;
  JobTimelineController(){
    _jobDatasource = locator<JobDatasource>();
  }

  bool _isLoading = false;
  bool get isLoading => _isLoading;
  set isLoading(bool value){
    _isLoading = value;
    notifyListeners();
  }
  get jobs => _jobs;

  Future loadJobs({bool notify = true}) async {
    try{
      isLoading = true;
      final List<Job> jobs = await _jobDatasource!.getJobByLimit(10);
      _jobs.clear();
      _jobs.addAll(jobs);
    }finally{
      isLoading = false;
    }
    if(notify){
      notifyListeners();
    }
  }
}