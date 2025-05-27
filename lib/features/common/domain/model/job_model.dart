
import 'dart:developer';

import 'package:intl/intl.dart';
import 'package:personalized_job_hunter/util/time/time_util.dart';

class Job{
  final String jobId;
  final String title;
  final String? location;
  final String? company;
  final String? jobPostedDate;
  final String? jobApplyLink;
  final String? jobDescription;
  final String? jobUrl;
  final String? companyWebsite;
  final String? jobLastDate;
  final String? experienceNeeded;
  final String? skillsNeeded;
  final String? companyIconUrl;
  final String? appliedAt;
  bool? applied;
  Job({
    required this.jobId,
    required this.title,
    this.location,
    this.jobPostedDate,
    this.jobApplyLink,
    this.company,
    this.companyWebsite,
    this.jobUrl,
    this.jobLastDate,
    this.experienceNeeded,
    this.jobDescription,
    this.skillsNeeded,
    this.companyIconUrl,
    this.applied,
    this.appliedAt
  });

  factory Job.fromJson(Map<String, dynamic> json) {
    log('iishanto: '+json.toString());
    log("jooo: ${json['appliedAt']}");

    return Job(
      jobId: json['jobId'] ?? '',
      title: json['title'] ?? 'Untitled Job',
      location: json['location'],
      jobPostedDate: json['jobPostedDate'],
      jobApplyLink: json['jobApplyLink'],
      company: json['company'],
      companyWebsite: json['companyWebsite'],
      jobUrl: json['jobUrl'],
      jobLastDate: json['jobLastDate'],
      experienceNeeded: json['experienceNeeded'],
      jobDescription: json['jobDescription'],
      skillsNeeded: json['skillsNeeded'],
      companyIconUrl: json['companyIconUrl'],
      applied: json['applied'],
      appliedAt: json['appliedAt']!=null && json['appliedAt'].toString().isNotEmpty ? toLocalTime(DateTime.parse(json['appliedAt'])): null
    );
  }

  get constructedIcon{
    if(companyWebsite!=null){
      return "https://www.google.com/s2/favicons?domain=${Uri.parse(companyWebsite!).host}&sz=64";
    }else{
      return null;
    }
  }

}