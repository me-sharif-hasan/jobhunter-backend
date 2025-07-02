import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';

class RoundedLogo extends StatelessWidget {
  String? imageUrl;
  RoundedLogo({super.key,this.imageUrl});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(8.0),
      decoration: BoxDecoration(
        color: Colors.white.withOpacity(0.9),
        shape: BoxShape.circle,
      ),
      child: imageUrl != null
          ? CachedNetworkImage(
        imageUrl: imageUrl!,
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