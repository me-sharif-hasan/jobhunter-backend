import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/widgets/loader.dart';
import 'package:personalized_job_hunter/features/common/widgets/search_bar.dart';
import 'package:personalized_job_hunter/features/job/controller/job_timeline_controller.dart';
import 'package:personalized_job_hunter/features/job/widget/job_card_shimmer.dart';
import 'package:provider/provider.dart';

import '../widget/job_timeline_card.dart';

class JobTimelineScreen extends StatefulWidget {
  const JobTimelineScreen({super.key});

  @override
  State<JobTimelineScreen> createState() => _JobTimelineScreenState();
}

class _JobTimelineScreenState extends State<JobTimelineScreen> with SingleTickerProviderStateMixin {
  final ScrollController _scrollController = ScrollController();
  late TabController _tabController;
  int _currentPage = 1;
  String _searchQuery = "";

  @override
  void initState() {
    log('JobTimelineScreen: initState');
    _tabController = TabController(length: 3, vsync: this);
    Provider.of<JobTimelineController>(context, listen: false).loadJobs();
    _scrollController.addListener(() {
      if (_scrollController.position.pixels == _scrollController.position.maxScrollExtent) {
        Provider.of<JobTimelineController>(context, listen: false)
            .loadJobs(
          currentPage: _currentPage,
          isSilent: true,
          searchQuery: _searchQuery,
        )
            .then((value) => {_currentPage++});
      }
    });
    super.initState();
  }

  @override
  void dispose() {
    _tabController.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<JobTimelineController>(
      builder: (context, controller, _) {
        return Scaffold(
          extendBody: true,
          body: Column(
            children: [
              // Merged Header (Tabs + Search Bar)
              _buildHeader(context, controller),
              // Site Filter
              if (controller.siteId != -1)
                Container(
                  height: 50,
                  color: Colors.grey[300],
                  padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 3),
                  child: Row(
                    children: [
                      Expanded(
                        child: Center(
                          child: controller.isLoading
                              ? const Text(
                            "Please wait",
                            textAlign: TextAlign.center,
                          )
                              : Text(
                            'Showing jobs from ${controller.currentlyFilteredSite}',
                            textAlign: TextAlign.center,
                          ),
                        ),
                      ),
                      IconButton(
                        onPressed: () {
                          controller.setSiteFilter(-1);
                        },
                        icon: const Icon(Icons.close),
                      ),
                    ],
                  ),
                ),
              // Job List
              Expanded(
                child: RefreshIndicator(
                  onRefresh: () async {
                    _currentPage = 1;
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
                          ? const CustomScrollView(
                        slivers: <Widget>[
                          SliverFillRemaining(
                            hasScrollBody: false,
                            child: Center(
                              child: Text(
                                'No jobs available, subscribe to companies to get jobs!',
                              ),
                            ),
                          ),
                        ],
                      )
                          : ListView.builder(
                        padding: const EdgeInsets.symmetric(vertical: 16.0),
                        itemCount: controller.jobs.length,
                        controller: _scrollController,
                        itemBuilder: (context, index) {
                          return controller.jobs[index].title=='Loading...'?const JobCardShimmer():JobTimelineCard(
                            job: controller.jobs[index],
                            isFirst: index == 0,
                            isLast: index == controller.jobs.length - 1,
                          );
                        },
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  Widget _buildHeader(BuildContext context, JobTimelineController controller) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 12.0),
      decoration: const BoxDecoration(
        color: Color(0xFFFDA626), // Solid purple for flat, modern look
      ),
      child: Column(
        children: [
          // Search Bar
          ModernSearchBar(
            onSearch: (query) {
              log('search query: $query');
              _currentPage = 1;
              _searchQuery = query;
              controller.loadJobs(searchQuery: query);
            },
          ),
          const SizedBox(height: 12),
          // Tabs
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              _buildTabButton(
                context,
                index: 0,
                icon: Icons.work,
                text: "All Jobs",
                isSelected: _tabController.index == 0,
                onTap: () {
                  _tabController.animateTo(0);
                  _currentPage = 1;
                  controller.filter = "all";
                },
              ),
              const SizedBox(width: 8),
              _buildTabButton(
                context,
                index: 1,
                icon: Icons.subscriptions,
                text: "Subscribed",
                isSelected: _tabController.index == 1,
                onTap: () {
                  _tabController.animateTo(1);
                  _currentPage = 1;
                  controller.filter="subscribed";
                },
              ),
              const SizedBox(width: 8),
              _buildTabButton(
                context,
                index: 2,
                icon: Icons.check_circle,
                text: "Applied",
                isSelected: _tabController.index == 2,
                onTap: () {
                  _tabController.animateTo(2);
                  _currentPage = 1;
                  controller.filter="applied";
                },
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildTabButton(
      BuildContext context, {
        required int index,
        required IconData icon,
        required String text,
        required bool isSelected,
        required VoidCallback onTap,
      }) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
        decoration: BoxDecoration(
          color: isSelected ? const Color(0xFFFFFFFF) : Colors.white.withOpacity(0.2),
          borderRadius: BorderRadius.circular(20),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(
              icon,
              size: 18,
              color: isSelected ? const Color(0xFFAB47BC) : Colors.white,
            ),
            const SizedBox(width: 6),
            Text(
              text,
              style: TextStyle(
                fontSize: 14,
                fontWeight: FontWeight.w600,
                color: isSelected ? const Color(0xFFAB47BC) : Colors.white,
              ),
            ),
          ],
        ),
      ),
    );
  }
}