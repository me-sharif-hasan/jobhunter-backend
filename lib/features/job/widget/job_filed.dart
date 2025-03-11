// lib/features/jobs/ui/job_field.dart
import 'package:flutter/material.dart';

class JobField extends StatelessWidget {
  final String label;
  final String value;
  final TextStyle? style;
  final int? maxLines;

  const JobField({
    super.key,
    required this.label,
    required this.value,
    this.style,
    this.maxLines,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          '$label: ',
          style: style?.copyWith(fontWeight: FontWeight.w600) ??
              Theme.of(context)
                  .textTheme
                  .bodySmall
                  ?.copyWith(fontWeight: FontWeight.w600),
        ),
        Expanded(
          child: Text(
            value,
            style: style ?? Theme.of(context).textTheme.bodySmall,
            maxLines: maxLines ?? 1,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ],
    );
  }
}