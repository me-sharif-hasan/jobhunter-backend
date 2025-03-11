import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/widgets/loader.dart';
import 'package:personalized_job_hunter/features/subscriptions/controller/site_controller.dart';
import 'package:personalized_job_hunter/features/subscriptions/widget/site_card.dart';
import 'package:provider/provider.dart';

import '../../common/widgets/search_bar.dart';

class CompanySubscriptionScreen extends StatefulWidget {
  const CompanySubscriptionScreen({super.key});

  @override
  State<CompanySubscriptionScreen> createState() =>
      _CompanySubscriptionScreenState();
}

class _CompanySubscriptionScreenState extends State<CompanySubscriptionScreen> {
  final ScrollController _scrollController = ScrollController();
  int _currentPage = 1;

  @override
  void initState() {
    log('CompanySubscriptionScreen: initState');
    _currentPage = 1;
    Provider.of<SiteController>(context, listen: false).getSites();
    _scrollController.addListener(() {
      if (_scrollController.position.pixels ==
          _scrollController.position.maxScrollExtent) {
        Provider.of<SiteController>(context, listen: false)
            .getSites(currentPage: _currentPage, isSilent: true)
            .then((value) {
          _currentPage++;
        });
      }
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<SiteController>(
      builder: (context, controller, _) {
        return Scaffold(
            extendBody: true,
            body: Column(
              children: [
                ModernSearchBar(
                  onSearch: (query) {
                    log('search query: $query');
                    _currentPage = 1;
                    controller.getSites(searchQuery: query);
                  },
                ),
                Expanded(
                  child: Loader(
                    isLoading: controller.isLoading,
                    child: RefreshIndicator(
                      onRefresh: () async {
                        _currentPage = 1;
                        await controller.getSites();
                      },
                      child: Container(
                        decoration: const BoxDecoration(
                          gradient: LinearGradient(
                            begin: Alignment.topLeft,
                            end: Alignment.bottomRight,
                            colors: [
                              Color(0xFFFFF7E6),
                              Color(0xFFFFE8D6),
                              Color(0xFFFFD6E7),
                            ],
                          ),
                        ),
                        child: controller.sites.isEmpty
                            ? const CustomScrollView(slivers: <Widget>[
                                SliverFillRemaining(
                                  hasScrollBody: false,
                                  child:
                                      Center(child: Text('No sites available')),
                                )
                              ])
                            : ListView.builder(
                                padding:
                                    const EdgeInsets.symmetric(vertical: 16.0),
                                itemCount: controller.sites.length,
                                controller: _scrollController,
                                itemBuilder: (context, index) {
                                  return SiteCard(
                                    isSubscribing: controller
                                                .subscriptionFunctionInProgressIndicator[
                                            controller.sites[index].id] ??
                                        false,
                                    site: controller.sites[index],
                                    subscribe: (int id) {
                                      Provider.of<SiteController>(context,
                                              listen: false)
                                          .subscribe(id)
                                          .then((value) {
                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(
                                          const SnackBar(
                                            content:
                                                Text('Subscribed successfully'),
                                          ),
                                        );
                                      }).catchError((e) {
                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(
                                          SnackBar(
                                            content: Text(e
                                                .toString()
                                                .replaceFirst("Exception:", "")
                                                .trim()),
                                          ),
                                        );
                                      });
                                    },
                                    unsubscribe: (int id) {
                                      Provider.of<SiteController>(context,
                                              listen: false)
                                          .unsubscribe(id)
                                          .then((value) {
                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(
                                          SnackBar(
                                            content: Text(
                                                'Unsubscribed from ${controller.sites[index].name}'),
                                          ),
                                        );
                                      }).catchError((e) {
                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(
                                          SnackBar(
                                            content: Text(e
                                                .toString()
                                                .replaceFirst("Exception:", "")
                                                .trim()),
                                          ),
                                        );
                                      });
                                    },
                                  );
                                },
                              ),
                      ),
                    ),
                  ),
                ),
              ],
            ));
      },
    );
  }
}
