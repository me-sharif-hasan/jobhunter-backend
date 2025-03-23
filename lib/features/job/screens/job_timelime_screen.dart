import 'dart:developer';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/widgets/loader.dart';
import 'package:personalized_job_hunter/features/common/widgets/search_bar.dart';
import 'package:personalized_job_hunter/features/job/controller/job_timeline_controller.dart';
import 'package:provider/provider.dart';

import '../widget/job_timeline_card.dart';

class JobTimelineScreen extends StatefulWidget {
  const JobTimelineScreen({super.key});

  @override
  State<JobTimelineScreen> createState() => _JobTimelineScreenState();
}

class _JobTimelineScreenState extends State<JobTimelineScreen> {
  ScrollController _scrollController = ScrollController();
  int _currentPage=1;
  String _searchQuery = "";
  @override
  void initState() {
    log('JobTimelineScreen: initState');
    Provider.of<JobTimelineController>(context, listen: false).loadJobs();
    _scrollController.addListener(() {
      if (_scrollController.position.pixels ==
          _scrollController.position.maxScrollExtent) {
        Provider.of<JobTimelineController>(context, listen: false)
            .loadJobs(
          currentPage: _currentPage,
          isSilent: true,
          searchQuery:_searchQuery
        ).then((value) => {
          _currentPage++
        });
      }
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<JobTimelineController>(
      builder: (context, controller, _) {
        return Scaffold(
          extendBody: true,
          body: Column(
            children: [
              ModernSearchBar(onSearch: (query){
                log('search query: $query');
                _currentPage=1;
                _searchQuery = query;
                controller.loadJobs(searchQuery: query);
              }),
              controller.siteId!=-1?Container(
                height: 50,
                color: Colors.grey[300],
                padding: const EdgeInsets.symmetric(horizontal: 16.0,vertical: 3),
                child: Row(
                  children: [
                    Expanded(
                      child: Center(
                        child: controller.isLoading?const Text("Please wait",textAlign: TextAlign.center,):Text('Showing jobs from ${controller.currentlyFilteredSite}',textAlign: TextAlign.center,),
                      ),
                    ),
                    IconButton(onPressed: (){
                      controller.setSiteFilter(-1);
                    }, icon: const Icon(Icons.close))
                  ],
                ),
              ):Container(),
              Expanded(
                child: RefreshIndicator(
                  onRefresh: () async {
                    _currentPage=1;
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
                        controller: _scrollController,
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
              )
            ]
          ),
        );
      },
    );
  }
}
