import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/widgets/loader.dart';
import 'package:personalized_job_hunter/features/job/controller/job_timeline_controller.dart';
import 'package:provider/provider.dart';

import '../widget/job_timeline_card.dart';

class JobTimelineScreen extends StatefulWidget {
  const JobTimelineScreen({super.key});

  @override
  State<JobTimelineScreen> createState() => _JobTimelineScreenState();
}

class _JobTimelineScreenState extends State<JobTimelineScreen> {
  @override
  void initState() {
    log('JobTimelineScreen: initState');
    Provider.of<JobTimelineController>(context, listen: false).loadJobs();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<JobTimelineController>(
      builder: (context, controller, _) {
        return Scaffold(
          extendBody: true,
          body: RefreshIndicator(
            onRefresh: () async {
              await controller.loadJobs();
            },
            child: Container(
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
              child: Loader(
                isLoading: controller.isLoading,
                child: controller.jobs.isEmpty
                    ? const CustomScrollView(slivers: <Widget>[
                        SliverFillRemaining(
                          hasScrollBody: false,
                          child: Center(child: Text('No jobs available, subscribe to companies to get jobs!')),
                        )
                      ])
                    : ListView.builder(
                        padding: const EdgeInsets.symmetric(vertical: 16.0),
                        itemCount: controller.jobs.length,
                        itemBuilder: (context, index) {
                          return JobTimelineCard(
                            job: controller.jobs[index],
                            isFirst: index == 0,
                            isLast: index == controller.jobs.length - 1,
                          );
                        },
                      ),
              ),
            ),
          ),
        );
      },
    );
  }
}
