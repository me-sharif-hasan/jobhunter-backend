// gradient_background.dart
import 'package:flutter/material.dart';

class GradientBackground extends StatelessWidget {
  final Widget child;

  const GradientBackground({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            Color(0xFFFFF7E6), // Soft golden yellow
            Color(0xFFFFE8D6), // Light peach
            Color(0xFFFFD6E7), // Pale pink
          ],
        ),
      ),
      child: child,
    );
  }
}