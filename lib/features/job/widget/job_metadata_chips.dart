import 'package:flutter/material.dart';
import '../../common/domain/model/job_model.dart';
import 'package:personalized_job_hunter/features/webview/inappwebview.dart';
import 'dart:io' show Platform;
import 'package:flutter/foundation.dart';

class JobMetadataChips extends StatelessWidget {
  final Job job;

  const JobMetadataChips({super.key, required this.job});

  void launchBrowser(BuildContext context, String url) {
    if (!kIsWeb && (Platform.isAndroid || Platform.isIOS)) {
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => JobWebViewScreen(
            job: job,
            url: url,
          ),
        ),
      );
    } else {
      print('Launching $url');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Wrap(
      spacing: 10.0,
      runSpacing: 8.0,
      children: [
        _buildAttributeChip(
          icon: Icons.location_on_outlined,
          text: job.location != null && job.location!.isNotEmpty
              ? "${job.location}"
              : 'Not specified',
        ),
        _buildAttributeChip(
          icon: Icons.calendar_today_outlined,
          text: job.jobLastDate != null && job.jobLastDate!.isNotEmpty
              ? (job.jobLastDate ?? "")
              : 'Unknown',
        ),
        _buildAttributeChip(
          icon: Icons.history,
          text: (job.jobPostedDate != null && job.jobPostedDate!.isNotEmpty)
              ? job.jobPostedDate ?? ""
              : "Not Available",
        ),
        _buildAttributeChip(
          icon: Icons.work,
          text: (job.experienceNeeded != null && job.experienceNeeded!.isNotEmpty)
              ? job.experienceNeeded ?? ""
              : "Not specified",
        ),
        if (job.companyWebsite != null)
          _buildWebsiteChip(
            text: job.companyWebsite!,
            onTap: () => launchBrowser(context, job.companyWebsite!),
          ),
      ],
    );
  }

  Widget _buildAttributeChip({required IconData icon, required String text}) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(6),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 16, color: Colors.white.withOpacity(0.9)),
          const SizedBox(width: 6),
          SizedBox(
            width: 250,
            child: Text(
              text,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 12,
              ),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildWebsiteChip({required String text, required VoidCallback onTap}) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
        decoration: BoxDecoration(
          color: const Color(0xFFFFA726).withOpacity(0.3),
          borderRadius: BorderRadius.circular(6),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Icons.link, size: 16, color: Colors.white.withOpacity(0.9)),
            const SizedBox(width: 6),
            SizedBox(
              width: 150,
              child: Text(
                text,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 12,
                ),
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
      ),
    );
  }
}