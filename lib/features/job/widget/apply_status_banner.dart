import 'dart:developer';
import 'package:flutter/material.dart';
import '../../common/domain/model/job_model.dart';

class ApplyStatusBanner extends StatefulWidget {
  final Job job;

  const ApplyStatusBanner({super.key, required this.job});

  @override
  State<ApplyStatusBanner> createState() => _ApplyStatusBannerState();
}

class _ApplyStatusBannerState extends State<ApplyStatusBanner> {
  String? _selectedStatus = 'Not Called';

  @override
  Widget build(BuildContext context) {
    if (!(widget.job.applied ?? false) ||
        widget.job.appliedAt == null ||
        widget.job.appliedAt!.isEmpty) {
      return const SizedBox.shrink();
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10.0, vertical: 6.0),
      decoration: BoxDecoration(
        gradient: const LinearGradient(
          begin: Alignment.centerLeft,
          end: Alignment.centerRight,
          colors: [
            Color(0xFFEF697A), // Pinkish-red
            Color(0xFFF0687A), // Orange-red
          ],
          stops: [0.0, 0.9],
        ),
        borderRadius: BorderRadius.circular(8),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.05),
            blurRadius: 4,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Icon(
                Icons.check_circle,
                size: 14,
                color: Color(0xFFCDD9FF), // Blue
              ),
              const SizedBox(width: 4),
              Text(
                "Applied: ${widget.job.appliedAt ?? 'Unknown'}",
                style: Theme.of(context).textTheme.bodySmall?.copyWith(
                  color: Colors.white,
                  fontSize: 11,
                  fontWeight: FontWeight.w600,
                  letterSpacing: 0.2,
                ),
              ),
            ],
          ),
          Container(
            height: 28,
            padding: const EdgeInsets.symmetric(horizontal: 8.0),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(2),
              border: Border.all(
                color: const Color(0xFFE32CFF).withOpacity(0.5), // Magenta
                width: 1,
              ),
            ),
            child: DropdownButtonHideUnderline(
              child: DropdownButton<String>(
                value: _selectedStatus,
                icon: const Icon(
                  Icons.arrow_drop_down,
                  color: Color(0xFFFFFFFF), // White
                  size: 18,
                ),
                elevation: 2,
                style: Theme.of(context).textTheme.bodySmall?.copyWith(
                  color: const Color(0xFFFFFFFF), // White
                  fontSize: 11,
                  fontWeight: FontWeight.w600,
                  letterSpacing: 0.2,
                ),
                dropdownColor: const Color(0xfffb512d), // Orange-red
                borderRadius: BorderRadius.circular(6),
                items: <String>['Called', 'Not Called', 'Rejected', 'Accepted']
                    .map<DropdownMenuItem<String>>((String value) {
                  return DropdownMenuItem<String>(
                    value: value,
                    child: Text(value),
                  );
                }).toList(),
                onChanged: (String? newValue) {
                  if (newValue != null) {
                    setState(() {
                      _selectedStatus = newValue;
                    });
                    log('Selected application status: $newValue');
                  }
                },
              ),
            ),
          ),
        ],
      ),
    );
  }
}