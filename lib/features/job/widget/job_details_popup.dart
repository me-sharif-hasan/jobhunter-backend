// lib/features/jobs/ui/job_details_popup.dart
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/widgets/apply_button.dart';
import '../../common/domain/model/job_model.dart';
import '../../webview/inappwebview.dart';

class JobDetailsPopup extends StatelessWidget {
  final Job job;

  const JobDetailsPopup({super.key, required this.job});

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            Color(0xFFFFA726), // Orange
            Color(0xFFFF7416), // Adjusted peach tone
          ],
        ),
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      child: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Header with Title and Apply Button
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                  child: Text(
                    job.title,
                    style: Theme.of(context).textTheme.titleLarge?.copyWith(
                      fontWeight: FontWeight.bold,
                      color: Colors.white,
                      fontSize: 20,
                    ),
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                ),
                if (job.jobUrl != null)
                  ApplyButton(job: job,applyButtonText: "Apply now",),
              ],
            ),
            const SizedBox(height: 16),
            // Job Details (excluding Job ID)
            if (job.company != null) _buildDetailRow('Company', job.company!),
            if (job.location != null) _buildDetailRow('Location', job.location!),
            if (job.jobPostedDate != null) _buildDetailRow('Posted Date', job.jobPostedDate!),
            if (job.jobLastDate != null) _buildDetailRow('Last Date', job.jobLastDate!),
            if (job.experienceNeeded != null) _buildDetailRow('Experience', job.experienceNeeded!),
            if (job.jobDescription != null) _buildDetailSection('Description', job.jobDescription!),
            if (job.skillsNeeded != null) _buildDetailSection('Skills Needed', job.skillsNeeded!),
            // Links after Skills
            if (job.jobUrl != null) _buildClickableDetail('Job URL', job.jobUrl!, () => _launchUrl(context, job.jobUrl!)),
            if (job.companyWebsite != null) _buildClickableDetail('Company Website', job.companyWebsite!, () => _launchUrl(context, job.companyWebsite!)),
            const SizedBox(height: 20),
          ],
        ),
      ),
    );
  }

  Widget _buildDetailRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            '$label: ',
            style: const TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.w600,
              fontSize: 14,
            ),
          ),
          Expanded(
            child: Text(
              value,
              style: TextStyle(
                color: Colors.white.withOpacity(0.9),
                fontSize: 14,
              ),
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDetailSection(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            label,
            style: const TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.w600,
              fontSize: 14,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            value,
            style: TextStyle(
              color: Colors.white.withOpacity(0.9),
              fontSize: 14,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildClickableDetail(String label, String value, VoidCallback onTap) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 6.0),
      child: GestureDetector(
        onTap: onTap,
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              '$label: ',
              style: const TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.w600,
                fontSize: 14,
              ),
            ),
            Expanded(
              child: Text(
                value,
                style: TextStyle(
                  color: Colors.white.withOpacity(0.9),
                  fontSize: 14,
                ),
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _launchUrl(BuildContext context, String url) {
    print('Launching $url');
    // Navigator.push(context, MaterialPageRoute(builder: (context) => JobWebViewScreen(job: job)));
  }
}