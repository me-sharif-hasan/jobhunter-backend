import 'dart:developer';
import 'dart:math' as math;

import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/domain/model/api_response.dart';
import 'package:personalized_job_hunter/features/common/domain/model/job_model.dart';
import 'package:personalized_job_hunter/util/values/widget_loading_registry.dart';
import 'package:provider/provider.dart';

import '../../common/controller/meta_controller.dart';
import '../domain/datasource/job_datasource.dart';
import '../domain/model/job_comment_model.dart';

class JobTimelineController extends ChangeNotifier {
  int siteId = -1;
  Map<String, List<JobCommentModel>> jobCommentMap = {};
  String currentlyFilteredSite = "All";
  String _currentFilterParam = "all";

  set filter(String filterValue) {
    _currentFilterParam = filterValue;
    loadJobs();
  }

  final List<Job> _jobs = [];

  JobDatasource? _jobDatasource;
  GetIt locator = GetIt.instance;

  JobTimelineController() {
    _jobDatasource = locator<JobDatasource>();
  }

  bool _isLoading = false;

  bool get isLoading => _isLoading;

  set isLoading(bool value) {
    _isLoading = value;
    notifyListeners();
  }

  get jobs => _jobs;

  Future loadJobs(
      {currentPage = 0,
      bool notify = true,
      bool isSilent = false,
      String searchQuery = ""}) async {
    try {
      if (siteId == -1 && MetaController.notificationPayload['id'] != null) {
        siteId = MetaController.notificationPayload['id'];
        MetaController.notificationPayload = {};
      }
      log("is silent $isSilent $currentPage");
      if (!isSilent) {
        _isLoading = true;
        WidgetsBinding.instance.addPostFrameCallback((_) {
          notifyListeners();
        });
      }
      if (isSilent) {
        if (MetaController.mainPageBuildContext != null) {
          Provider.of<MetaController>(MetaController.mainPageBuildContext!,
                  listen: false)
              .loadingData = true;
        }
      }
      Job loadingJob = Job(
        jobId: "loading...",
        title: "Loading...",
        company: "Loading...",
        jobUrl: "",
        jobPostedDate: "",
        jobLastDate: "",
        location: "",
        experienceNeeded: "",
        applied: false,
      );
      //if not added already
      if (!_jobs.any((job) => job.jobId == "loading...")) {
        _jobs.add(loadingJob);
      }
      WidgetsBinding.instance.addPostFrameCallback((_) {
        notifyListeners();
      });
      final List<Job> jobs = await _jobDatasource!.getJobByLimit(
          10, currentPage, searchQuery, siteId, _currentFilterParam);
      _jobs.removeWhere((job) => job.jobId == "loading...");
      if (jobs.isEmpty && isSilent) {
        throw Exception("No more jobs");
      }
      if (!isSilent) {
        _jobs.clear();
      }
      _jobs.addAll(jobs);
      if (siteId != -1 && _jobs.isNotEmpty) {
        currentlyFilteredSite = _jobs[0].company ?? "All";
      } else if (siteId == -1) {
        currentlyFilteredSite = "All";
      }
    } catch (e) {
      log("ii logs Exception: $e");
    } finally {
      isLoading = false;
      if (isSilent) {
        if (MetaController.mainPageBuildContext != null) {
          Provider.of<MetaController>(MetaController.mainPageBuildContext!,
                  listen: false)
              .loadingData = false;
        }
      }
    }
    if (notify) {
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

  void applyForJob(Job job) {
    log("Marking started");
    if (MetaController.mainPageBuildContext != null) {
      log("Setting main page build context");
      Provider.of<MetaController>(MetaController.mainPageBuildContext!,
              listen: false)
          .loadingData = true;
      Provider.of<MetaController>(MetaController.mainPageBuildContext!,
              listen: false)
          .setLoading(WidgetLoadingRegistry.apply_button, meta: job.jobId);
    }
    log("${job.applied}");
    if (!(job.applied ?? false)) {
      _jobDatasource!.markJobAsApplied(job.jobId).then((apiResponse) {
        if (apiResponse.success) {
          log("Applied successfully!");
          for (Job aJob in jobs) {
            if (job.jobId == aJob.jobId) {
              aJob.applied = true;
              notifyListeners();
              break;
            }
          }
        } else {
          log("ERROR APPLYING");
        }
      }).then((_) {
        if (MetaController.mainPageBuildContext != null) {
          Provider.of<MetaController>(MetaController.mainPageBuildContext!,
                  listen: false)
              .loadingData = false;
          Provider.of<MetaController>(MetaController.mainPageBuildContext!,
                  listen: false)
              .unsetLoading(WidgetLoadingRegistry.apply_button);
        }
      });
    } else {
      _jobDatasource!.unmarkAJob(job.jobId).then((apiResponse) {
        if (apiResponse.success) {
          log("Applied successfully!");
          for (Job aJob in jobs) {
            if (job.jobId == aJob.jobId) {
              aJob.applied = false;
              notifyListeners();
              break;
            }
          }
        } else {
          log("ERROR APPLYING");
        }
      }).then((_) {
        if (MetaController.mainPageBuildContext != null) {
          Provider.of<MetaController>(MetaController.mainPageBuildContext!,
                  listen: false)
              .loadingData = false;
          Provider.of<MetaController>(MetaController.mainPageBuildContext!,
                  listen: false)
              .unsetLoading(WidgetLoadingRegistry.apply_button);
        }
      });
    }
  }

  Future<void> updateJobApplicationStatus(
      Job job, JobApplyStatus applyStatus) async {
    log("Updating job application status: ${job.jobId}, isApplied: $applyStatus");
    await _jobDatasource!
        .updateJobApplicationStatus(job.jobId, applyStatus)
        .then((apiResponse) {
      if (apiResponse.success) {
        log("Job application status updated successfully");
        for (Job aJob in jobs) {
          if (job.jobId == aJob.jobId) {
            aJob.applicationStatus = applyStatus;
            notifyListeners();
            break;
          }
        }
      } else {
        log("Error updating job application status");
      }
    }).catchError((error) {
      log("Error updating job application status: $error");
    });

    if (MetaController.mainPageBuildContext != null) {
      Provider.of<MetaController>(MetaController.mainPageBuildContext!,
              listen: false)
          .unsetLoading(WidgetLoadingRegistry.apply_button);
    }
  }

  Future<List<JobCommentModel>> loadComments(String jobId,{bool fullLoad = false}) async {
    if(!fullLoad && jobCommentMap[jobId]!=null) {
      return [];
    }
    if (jobCommentMap[jobId] == null) {
      jobCommentMap[jobId] = [];
    }
    List<JobCommentModel> jobs = jobCommentMap[jobId]!;
    int startAt = 0;
    for (JobCommentModel jobCommentModel in jobs) {
      startAt = math.max(jobCommentModel.createTime.millisecondsSinceEpoch, startAt);
    }
    List<JobCommentModel> jobComments =
    await _jobDatasource!.loadJobComments(jobId, startAt, 10);
    jobCommentMap[jobId]!.addAll(jobComments);
    notifyListeners();
    return jobComments;
  }

  void toggleFavorite(Job job) {}

  void addComment(String jobId, JobCommentModel jobCommentModel) {}
}
