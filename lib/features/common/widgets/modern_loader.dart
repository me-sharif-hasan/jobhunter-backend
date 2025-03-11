// lib/common/widgets/modern_loader.dart
import 'package:flutter/material.dart';
import 'dart:math' as math;

class ModernLoader extends StatefulWidget {
  final double size;
  final List<Color>? gradientColors;

  const ModernLoader({
    super.key,
    this.size = 70.0, // Increased default size
    this.gradientColors,
  });

  @override
  State<ModernLoader> createState() => _ModernLoaderState();
}

class _ModernLoaderState extends State<ModernLoader> with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _rotationAnimation;
  late Animation<double> _bloomAnimation;
  late Animation<double> _innerRotationAnimation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: const Duration(milliseconds: 2500), // Slower for smoother flow
      vsync: this,
    )..repeat();

    // Outer rotation (clockwise)
    _rotationAnimation = Tween<double>(begin: 0, end: 2 * math.pi).animate(
      CurvedAnimation(
        parent: _controller,
        curve: const Interval(0.0, 1.0, curve: Curves.easeInOutQuad), // Gentler curve
      ),
    );

    // Inner rotation (counter-clockwise for contrast)
    _innerRotationAnimation = Tween<double>(begin: 2 * math.pi, end: 0).animate(
      CurvedAnimation(
        parent: _controller,
        curve: const Interval(0.0, 1.0, curve: Curves.easeInOutQuad),
      ),
    );

    // Blooming effect (expand and contract)
    _bloomAnimation = TweenSequence<double>([
      TweenSequenceItem(tween: Tween<double>(begin: 0.9, end: 1.3), weight: 50),
      TweenSequenceItem(tween: Tween<double>(begin: 1.3, end: 0.9), weight: 50),
    ]).animate(
      CurvedAnimation(
        parent: _controller,
        curve: Curves.easeInOutQuad, // Smoother blooming
      ),
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final defaultColors = [
      const Color(0xFFFFA726),
      const Color(0xFFFF8C00),
      const Color(0xFFFFA726).withOpacity(0.4),
    ];

    return AnimatedBuilder(
      animation: _controller,
      builder: (context, child) {
        return SizedBox(
          width: widget.size,
          height: widget.size,
          child: CustomPaint(
            painter: _BloomingLoaderPainter(
              rotation: _rotationAnimation.value,
              innerRotation: _innerRotationAnimation.value,
              bloom: _bloomAnimation.value,
              gradientColors: widget.gradientColors ?? defaultColors,
              loaderSize: widget.size,
            ),
          ),
        );
      },
    );
  }
}

class _BloomingLoaderPainter extends CustomPainter {
  final double rotation;
  final double innerRotation;
  final double bloom;
  final List<Color> gradientColors;
  final double loaderSize;

  _BloomingLoaderPainter({
    required this.rotation,
    required this.innerRotation,
    required this.bloom,
    required this.gradientColors,
    required this.loaderSize,
  });

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final baseRadius = loaderSize / 3; // Slightly larger base radius for circularity
    final strokeWidth = loaderSize / 12; // Adjusted for bigger size

    // Outer ring (clockwise rotation)
    final outerPaint = Paint()
      ..style = PaintingStyle.stroke
      ..strokeWidth = strokeWidth
      ..strokeCap = StrokeCap.round
      ..shader = SweepGradient(
        colors: gradientColors,
        transform: GradientRotation(rotation),
      ).createShader(Rect.fromCircle(center: center, radius: baseRadius * bloom));

    canvas.drawArc(
      Rect.fromCircle(center: center, radius: baseRadius * bloom),
      rotation,
      2 * math.pi * 0.75, // 75% of a full circle for a flowing effect
      false,
      outerPaint,
    );

    // Inner ring (counter-clockwise rotation, slightly smaller)
    final innerRadius = baseRadius * 0.7 * bloom;
    final innerPaint = Paint()
      ..style = PaintingStyle.stroke
      ..strokeWidth = strokeWidth * 0.8
      ..strokeCap = StrokeCap.round
      ..shader = SweepGradient(
        colors: gradientColors.reversed.toList(), // Reverse gradient for contrast
        transform: GradientRotation(innerRotation),
      ).createShader(Rect.fromCircle(center: center, radius: innerRadius));

    canvas.drawArc(
      Rect.fromCircle(center: center, radius: innerRadius),
      innerRotation,
      2 * math.pi * 0.6, // 60% of a circle for variation
      false,
      innerPaint,
    );

    // Central glow (subtle core)
    final corePaint = Paint()
      ..style = PaintingStyle.fill
      ..shader = RadialGradient(
        colors: [
          const Color(0xFFFFA726).withOpacity(0.3),
          Colors.transparent,
        ],
      ).createShader(Rect.fromCircle(center: center, radius: loaderSize / 10));
    canvas.drawCircle(center, loaderSize / 15, corePaint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => true;
}