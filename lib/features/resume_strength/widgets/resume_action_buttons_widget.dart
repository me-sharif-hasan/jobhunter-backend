import 'package:flutter/material.dart';

class ResumeActionButtonsWidget extends StatelessWidget {
  final VoidCallback? onDownloadReport;
  final VoidCallback? onShareReport;
  final VoidCallback? onViewDetails;

  const ResumeActionButtonsWidget({
    super.key,
    this.onDownloadReport,
    this.onShareReport,
    this.onViewDetails,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: Row(
        children: [
          Expanded(
            child: ElevatedButton.icon(
              onPressed: onViewDetails,
              icon: const Icon(Icons.visibility_outlined),
              label: const Text('View Details'),
              style: ElevatedButton.styleFrom(
                padding: const EdgeInsets.symmetric(vertical: 12),
                backgroundColor: const Color(0xFFFFA726),
                foregroundColor: Colors.white,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                elevation: 2,
              ),
            ),
          ),
          const SizedBox(width: 12),
          OutlinedButton.icon(
            onPressed: onShareReport,
            icon: const Icon(Icons.share_outlined),
            label: const Text('Share'),
            style: OutlinedButton.styleFrom(
              padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 16),
              foregroundColor: const Color(0xFFFFA726),
              side: const BorderSide(color: Color(0xFFFFA726)),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
