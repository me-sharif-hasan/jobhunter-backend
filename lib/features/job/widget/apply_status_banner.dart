import 'dart:developer';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:personalized_job_hunter/features/job/controller/job_timeline_controller.dart';
import 'package:provider/provider.dart';
import '../../common/domain/model/job_model.dart';

class ApplyStatusBanner extends StatefulWidget {
  final Job job;

  const ApplyStatusBanner({super.key, required this.job});

  @override
  State<ApplyStatusBanner> createState() => _ApplyStatusBannerState();
}

class _ApplyStatusBannerState extends State<ApplyStatusBanner> {
  List<JobApplyStatus> get _statusOptions =>
      Provider.of<MetaController>(context, listen: false).jobAppliedOptions;
  bool _isUpdating = false;

  @override
  Widget build(BuildContext context) {
    if (!(widget.job.applied ?? false) ||
        widget.job.appliedAt == null ||
        widget.job.appliedAt!.isEmpty) {
      return const SizedBox.shrink();
    }

    return Align(
      alignment: Alignment.topRight,
      child: Container(
        margin: const EdgeInsets.all(8.0),
        padding: const EdgeInsets.symmetric(horizontal: 12.0, vertical: 8.0),
        decoration: BoxDecoration(
          color: const Color(0xFF2A2A2A), // Dark background like YouTube
          borderRadius: BorderRadius.circular(20),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.2),
              blurRadius: 6,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            _isUpdating || Provider.of<MetaController>(context).jobAppliedOptions.isEmpty
                ? const SizedBox(
                    width: 14,
                    height: 14,
                    child: CircularProgressIndicator(
                      strokeWidth: 2,
                      color: Color(0xFFCDD9FF), // Blue
                    ),
                  )
                : const Icon(
                    Icons.info_outline,
                    size: 14,
                    color: Color(0xFFCDD9FF), // Blue
                  ),
            const SizedBox(width: 6),
            Text(
              "${widget.job.appliedAt ?? 'Unknown'}",
              style: Theme.of(context).textTheme.bodySmall?.copyWith(
                    color: Colors.white,
                    fontSize: 10,
                    fontWeight: FontWeight.w500,
                  ),
            ),
            const SizedBox(width: 8),
            Container(
              height: 24,
              padding: const EdgeInsets.symmetric(horizontal: 6.0),
              decoration: BoxDecoration(
                color: const Color(0xFF3A3A3A), // Slightly lighter dark shade
                borderRadius: BorderRadius.circular(12),
              ),
              child: DropdownButtonHideUnderline(
                child: DropdownButton<JobApplyStatus>(
                  value: widget.job.applicationStatus,
                  icon: const Icon(
                    Icons.arrow_drop_down,
                    color: Colors.white,
                    size: 16,
                  ),
                  style: Theme.of(context).textTheme.bodySmall?.copyWith(
                        color: Colors.white,
                        fontSize: 10,
                        fontWeight: FontWeight.w500,
                      ),
                  dropdownColor: const Color(0xFF3A3A3A), // Matches container
                  borderRadius: BorderRadius.circular(12),
                  items: Provider.of<MetaController>(context).jobAppliedOptions.map<DropdownMenuItem<JobApplyStatus>>(
                          (JobApplyStatus value) {
                    return DropdownMenuItem<JobApplyStatus>(
                      value: value,
                      child: Text(value.name),
                    );
                  }).toList(),
                  onChanged: (JobApplyStatus? newValue) async {
                    if (newValue != null) {
                      setState(() {
                        _isUpdating = true;
                      });
                      try {
                        await Provider.of<JobTimelineController>(context,
                                listen: false)
                            .updateJobApplicationStatus(
                          widget.job,
                          newValue,
                        );
                        log('Selected application status: $newValue');
                      } finally {
                        if (mounted) {
                          setState(() {
                            _isUpdating = false;
                          });
                        }
                      }
                    }
                  },
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}