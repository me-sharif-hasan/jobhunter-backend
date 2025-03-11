// lib/features/jobs/ui/timeline_dot.dart
import 'package:flutter/material.dart';

class TimelineDot extends StatelessWidget {
  final bool isFirst;
  final bool isLast;

  const TimelineDot({
    super.key,
    this.isFirst = false,
    this.isLast = false,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: 16,
      child: Stack(
        alignment: Alignment.center,
        children: [
          // Vertical line (thinner and softer color)
          if (!isFirst || !isLast)
            Column(
              children: [
                if (!isFirst)
                  Expanded(
                    child: Container(
                      width: 1.5, // Thinner line
                      color: const Color(0xFFFFA726).withOpacity(0.5),
                    ),
                  ),
                if (!isLast)
                  Expanded(
                    child: Container(
                      width: 1.5,
                      color: const Color(0xFFFFA726).withOpacity(0.5),
                    ),
                  ),
              ],
            ),
          // Dot (smaller with gradient)
          Container(
            width: 8,
            height: 8,
            decoration: const BoxDecoration(
              shape: BoxShape.circle,
              gradient: LinearGradient(
                colors: [
                  Color(0xFFFFA726),
                  Color(0xFFFF8C00),
                ],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
              ),
            ),
          ),
        ],
      ),
    );
  }
}