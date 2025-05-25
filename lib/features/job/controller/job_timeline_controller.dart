import 'dart:developer';

import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/domain/model/job_model.dart';
import 'package:provider/provider.dart';

import '../../common/controller/meta_controller.dart';
import '../domain/datasource/job_datasource.dart';

class JobTimelineController extends ChangeNotifier{
  int siteId=-1;
  String currentlyFilteredSite="All";
  String _currentFilterParam="all";
  set filter(String filterValue){
    _currentFilterParam=filterValue;
    loadJobs();
  }
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

  Future loadJobs({currentPage=0,bool notify = true, bool isSilent=false, String searchQuery=""}) async {
    try{
      if(siteId==-1&&MetaController.notificationPayload['id']!=null){
        siteId = MetaController.notificationPayload['id'];
        MetaController.notificationPayload = {};
      }
      log("is silent $isSilent $currentPage");
      if(!isSilent){
        _isLoading = true;
        notifyListeners();
      }
      if(isSilent){
        if(MetaController.mainPageBuildContext!=null){
          Provider.of<MetaController>(MetaController.mainPageBuildContext!, listen: false).loadingData = true;
        }
      }
      final List<Job> jobs = await _jobDatasource!.getJobByLimit(10,currentPage,searchQuery,siteId,_currentFilterParam);
      if(jobs.isEmpty&&isSilent){
        throw Exception("No more jobs");
      }
      if(!isSilent){
        _jobs.clear();
      }
      _jobs.addAll(jobs);
      if(siteId!=-1&&_jobs.isNotEmpty){
        currentlyFilteredSite = _jobs[0].company??"All";
      }else if(siteId==-1){
        currentlyFilteredSite = "All";
      }
    }catch(e){
      log("ii logs Exception: $e");
    }finally{
      isLoading = false;
      if(isSilent) {
        if (MetaController.mainPageBuildContext != null) {
          Provider
              .of<MetaController>(
              MetaController.mainPageBuildContext!, listen: false)
              .loadingData = false;
        }
      }
    }
    if(notify){
      notifyListeners();
    }
  }

  void setSiteFilter(int siteId) {
    print('setSiteFilter: $siteId');
    this.siteId = siteId;
    MetaController.notificationPayload = {};
    loadJobs();
    notifyListeners();
  }

  void applyForJob(Job job){
    log("Marking started");
    if(MetaController.mainPageBuildContext!=null){
      log("Setting main page build context");
      Provider.of<MetaController>(MetaController.mainPageBuildContext!, listen: false).loadingData = true;
    }
    log("${job.applied}");
    if(!(job.applied??false)){
      _jobDatasource!.markJobAsApplied(job.jobId).then((apiResponse){
        if(apiResponse.success){
          log("Applied successfully!");
          for(Job aJob in jobs){
            if(job.jobId==aJob.jobId){
              aJob.applied=true;
              notifyListeners();
              break;
            }
          }
        }else{
          log("ERROR APPLYING");
        }
      }).then((_){
        if(MetaController.mainPageBuildContext!=null){
          Provider.of<MetaController>(MetaController.mainPageBuildContext!, listen: false).loadingData = false;
        }
      });
    }else{
      _jobDatasource!.unmarkAJob(job.jobId).then((apiResponse){
        if(apiResponse.success){
          log("Applied successfully!");
          for(Job aJob in jobs){
            if(job.jobId==aJob.jobId){
              aJob.applied=false;
              notifyListeners();
              break;
            }
          }
        }else{
          log("ERROR APPLYING");
        }
      }).then((_){
        if(MetaController.mainPageBuildContext!=null){
          Provider.of<MetaController>(MetaController.mainPageBuildContext!, listen: false).loadingData = false;
        }
      });
    }
  }
}