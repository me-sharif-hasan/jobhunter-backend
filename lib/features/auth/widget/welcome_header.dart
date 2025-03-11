// welcome_header.dart
import 'package:flutter/material.dart';

class WelcomeHeader extends StatelessWidget {
  final String? title;
  final String? subtitle;

  const WelcomeHeader({
    super.key,
    this.title,
    this.subtitle,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        const Icon(
          Icons.lightbulb_outline,
          size: 80,
          color: Color(0xFFFFA726),
        ),
        const SizedBox(height: 16),
        Text(
          title ?? 'Find Your Dream Job',
          style: Theme.of(context).textTheme.headlineMedium?.copyWith(
            fontWeight: FontWeight.bold,
            color: const Color(0xFF4A4A4A),
          ),
        ),
        const SizedBox(height: 8),
        Text(
          subtitle ?? 'Let’s make your future bright!',
          style: Theme.of(context).textTheme.bodyLarge?.copyWith(
            color: Colors.grey.shade600,
          ),
        ),
      ],
    );
  }
}