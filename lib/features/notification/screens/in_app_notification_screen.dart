import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:personalized_job_hunter/features/common/widgets/loader.dart';
import 'package:personalized_job_hunter/features/notification/controller/in_app_notification_controller.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';
import 'package:provider/provider.dart';

import '../widget/notification_card_widget.dart'; // For formatting timestamps

class InAppNotificationScreen extends StatefulWidget {
  const InAppNotificationScreen({super.key});

  @override
  State<InAppNotificationScreen> createState() =>
      _InAppNotificationScreenState();
}

class _InAppNotificationScreenState extends State<InAppNotificationScreen> {
  @override
  void initState() {
    Provider.of<InAppNotificationController>(context, listen: false)
        .loadNotifications();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<InAppNotificationController>(
        builder: (context, controller, _) {
      return Scaffold(
        backgroundColor: Colors.transparent,
        body: Container(
          decoration: BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topCenter,
              end: Alignment.bottomCenter,
              colors: [
                ...Constants.getThemeColor(Provider.of<MetaController>(context)
                    .currentPage) // Light Purple
              ],
            ),
          ),
          child: SafeArea(
            child: Column(
              children: [
                // Header
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      const Text(
                        'Notifications',
                        style: TextStyle(
                          fontSize: 28,
                          fontWeight: FontWeight.w600,
                          color: Colors.white,
                        ),
                      ),
                      IconButton(
                        icon: const Icon(Icons.clear_all, color: Colors.white),
                        onPressed: () {
                          // Placeholder for clearing notifications
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(
                                content: Text('Notifications cleared')),
                          );
                        },
                      ),
                    ],
                  ),
                ),
                // Notification List
                Expanded(
                  child: RefreshIndicator(
                    onRefresh: () async {
                      controller.loadNotifications();
                    },
                    child: Loader(
                      isLoading: controller.isLoading,
                      child: controller.notifications.isEmpty
                          ? const CustomScrollView(
                              slivers: [
                                SliverFillRemaining(
                                  hasScrollBody: false,
                                  child: Center(
                                    child: Text(
                                      'No notifications yet',
                                      style: TextStyle(
                                        fontSize: 18,
                                        color: Colors.white70,
                                      ),
                                    ),
                                  ),
                                ),
                              ],
                            )
                          : ListView.builder(
                              padding: const EdgeInsets.symmetric(
                                  horizontal: 16.0, vertical: 8.0),
                              itemCount: controller.notifications.length,
                              itemBuilder: (context, index) {
                                final notification =
                                    controller.notifications[index];
                                return NotificationCard(
                                    notification: notification);
                              },
                            ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      );
    });
  }
}

// Notification Card Widget

// Data Model
