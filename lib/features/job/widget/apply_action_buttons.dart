import 'dart:developer';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/widgets/apply_button.dart';
import 'package:personalized_job_hunter/features/job/controller/job_timeline_controller.dart';
import 'package:provider/provider.dart';
import '../../common/domain/model/job_model.dart';

class ApplyActionButtons extends StatelessWidget {
  final Job job;

  const ApplyActionButtons({super.key, required this.job});

  @override
  Widget build(BuildContext context) {
    if ((job.jobUrl ?? job.jobApplyLink) == null) {
      return const SizedBox.shrink();
    }

    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Flexible(
          child: ApplyButton(
            job: job,
            buttonBorder: const BorderRadius.only(
                topLeft: Radius.circular(8), bottomLeft: Radius.circular(8)),
            icon: const Icon(
              Icons.arrow_forward,
              color: Colors.blue,
            ),
          ),
        ),
        Flexible(
          child: ElevatedButton.icon(
            onPressed: () {
              log("Marking in progress");
              Provider.of<JobTimelineController>(context, listen: false)
                  .applyForJob(job);
            },
            label: Text((job.applied ?? false) == false ? "Mark as applied" : "Applied"),
            icon: (job.applied ?? false)
                ? const Icon(
              Icons.check_circle,
              color: Colors.green,
            )
                : const Icon(
              Icons.check_circle_outline,
              color: Colors.green,
            ),
            style: ElevatedButton.styleFrom(
              padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 8),
              backgroundColor: Colors.white,
              foregroundColor: Colors.green,
              shape: const RoundedRectangleBorder(
                borderRadius: BorderRadius.only(
                    topRight: Radius.circular(8), bottomRight: Radius.circular(8)),
              ),
              elevation: 0,
              textStyle: const TextStyle(
                fontSize: 14,
              ),
            ).copyWith(
              overlayColor: WidgetStateProperty.all(
                const Color(0xFFFFA726).withOpacity(0.1),
              ),
            ),
          ),
        ),
      ],
    );
  }
}