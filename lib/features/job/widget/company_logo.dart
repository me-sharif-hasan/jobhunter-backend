import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import '../../common/domain/model/job_model.dart';

class CompanyLogo extends StatelessWidget {
  final Job job;

  const CompanyLogo({super.key, required this.job});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(8.0),
      decoration: BoxDecoration(
        color: Colors.white.withOpacity(0.9),
        shape: BoxShape.circle,
      ),
      child: job.constructedIcon != null
          ? CachedNetworkImage(
        imageUrl: job.constructedIcon!,
        width: 30,
        height: 30,
        fit: BoxFit.contain,
        errorWidget: (context, error, stackTrace) => const Icon(
          Icons.business,
          size: 30,
          color: Color(0xFFFFA726),
        ),
      )
          : const Icon(
        Icons.business,
        size: 30,
        color: Color(0xFFFFA726),
      ),
    );
  }
}