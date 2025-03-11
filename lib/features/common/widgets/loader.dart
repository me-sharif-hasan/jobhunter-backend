import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/widgets/modern_loader.dart';

class Loader extends StatefulWidget {
  bool isLoading;
  Widget child;
  Loader({super.key, required this.isLoading, required this.child});

  @override
  State<Loader> createState() => _LoaderState();
}

class _LoaderState extends State<Loader> {
  @override
  Widget build(BuildContext context) {
    return widget.isLoading
        ? const Center(
            child: ModernLoader(),
          )
        : widget.child;
  }
}
