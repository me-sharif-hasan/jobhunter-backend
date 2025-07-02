import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/widgets/rounded_logo.dart';
import '../../common/domain/model/job_model.dart';

class CompanyLogo extends StatelessWidget {
  final Job job;

  const CompanyLogo({super.key, required this.job});

  @override
  Widget build(BuildContext context) {
    return RoundedLogo(
      imageUrl: job.constructedIcon,
    );
  }
}