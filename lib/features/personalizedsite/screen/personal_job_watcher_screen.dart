import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:personalized_job_hunter/features/common/widgets/loader.dart';
import 'package:personalized_job_hunter/features/common/widgets/rounded_logo.dart';
import 'package:personalized_job_hunter/features/personalizedsite/controller/personal_site_controller.dart';
import 'package:personalized_job_hunter/features/personalizedsite/widgets/add_site_widget.dart';
import 'package:personalized_job_hunter/features/subscriptions/domain/models/site_model.dart';
import 'package:provider/provider.dart';

class PersonalJobWatcherScreen extends StatefulWidget {
  const PersonalJobWatcherScreen({super.key});

  @override
  State<PersonalJobWatcherScreen> createState() => _PersonalJobWatcherScreenState();
}

class _PersonalJobWatcherScreenState extends State<PersonalJobWatcherScreen> with SingleTickerProviderStateMixin {
  final TextEditingController _urlController = TextEditingController();
  late AnimationController _controller;

  @override
  void initState() {
    Provider.of<Personalsitecontroller>(context, listen: false).loadPersonalSites(context);
    super.initState();
    _controller = AnimationController(
      duration: const Duration(milliseconds: 200),
      vsync: this,
    );
  }

  @override
  void dispose() {
    _urlController.dispose();
    _controller.dispose();
    super.dispose();
  }

  String _formatTimestamp(DateTime timestamp) {
    final now = DateTime.now();
    final difference = now.difference(timestamp);
    if (difference.inMinutes < 60) {
      return '${difference.inMinutes} minutes ago';
    } else if (difference.inHours < 24) {
      return '${difference.inHours} hours ago';
    } else {
      return DateFormat('MMM d, h:mm a').format(timestamp);
    }
  }

  void _toggleAddSiteWidget() {
    AddSiteWidget.show(context, _handleAddSite);
  }

  Future<void> _handleAddSite(String homepage, String careerPage) async {
    try {
      final controller = Provider.of<Personalsitecontroller>(context, listen: false);
      
      // Call the controller method to add the site
      Site? newSite = await controller.addPersonalSite(homepage, careerPage);
      
      if (newSite != null) {
        // Refresh the sites list to show the new site
        await controller.refreshSites(context);
        
        // Show success message
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Site "${newSite.name}" added successfully!'),
              backgroundColor: Provider.of<MetaController>(context, listen: false).getThemeColor()[0],
              behavior: SnackBarBehavior.floating,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
              duration: const Duration(seconds: 3),
            ),
          );
        }
      }
    } catch (error) {
      // Re-throw the error to be handled by the widget
      throw error;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<Personalsitecontroller>(builder: (context,controller,_){
      return Scaffold(
      backgroundColor: Provider.of<MetaController>(context).getThemeColor()[0], // Dark Slate Grey
      appBar: AppBar(
        title: Text(
          Provider.of<MetaController>(context,listen: false).currentScreenTitle,
          style: const TextStyle(
            fontSize: 28,
            fontWeight: FontWeight.w600,
            color: Colors.white,
          ),
        ),
        backgroundColor: Provider.of<MetaController>(context, listen: false).getThemeColor()[0],
        elevation: 0,
      ),
      body: Stack(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: RefreshIndicator(
              onRefresh: () async {
                await controller.refreshSites(context);
              },
              child: Column(
                children: [
                  // Input Section
                  // ModernSearchBar(onSearch: (String query){}),
                  // const SizedBox(height: 12),                  // Site List
                  Loader(
                    isLoading: Provider.of<Personalsitecontroller>(context).isLoading,
                    child: Expanded(
                      child: controller.personalSites.isEmpty
                          ? const CustomScrollView(
                            slivers: <Widget>[
                          SliverFillRemaining(
                            hasScrollBody: false,
                            child: Center(
                              child: Center(
                                child: Text(
                                  'No sites added yet',
                                  style: TextStyle(
                                    color: Color(0xFFCE93D8), // Soft Lavender
                                    fontSize: 14,
                                  ),
                                ),
                              )))]
                          )
                          : ListView.builder(
                              itemCount: controller.personalSites.length,
                              itemBuilder: (context, index) {
                                final site = controller.personalSites[index];
                                return Container(
                                  margin: const EdgeInsets.symmetric(vertical: 4),
                                  padding: const EdgeInsets.all(12),
                                  decoration: BoxDecoration(
                                    color: Color(0xFFCFD8DC).withOpacity(0.1), // Warm Grey
                                    borderRadius: BorderRadius.circular(16),
                                    boxShadow: [
                                      BoxShadow(
                                        color: Colors.black.withOpacity(0.03),
                                        blurRadius: 4,
                                        offset: const Offset(0, 1),
                                      ),
                                    ],
                                  ),
                                  child: Row(
                                    children: [
                                      RoundedLogo(imageUrl: site.constructedIcon,),
                                      const SizedBox(width: 12),
                                      Expanded(
                                        child: Column(
                                          crossAxisAlignment: CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              site.name,
                                              style: const TextStyle(
                                                fontSize: 16,
                                                fontWeight: FontWeight.w600,
                                                color: Color(0xFFCFD8DC), // Warm Grey
                                              ),
                                            ),
                                            const SizedBox(height: 4),
                                            Text(
                                              'Last crawled: ${_formatTimestamp(site.lastCrawledAt)}',
                                              style: const TextStyle(
                                                fontSize: 12,
                                                color: Color(0xFFCE93D8), // Soft Lavender
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                      Container(
                                        padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                                        decoration: BoxDecoration(
                                          gradient: const LinearGradient(
                                            colors: [Color.fromARGB(255, 255, 255, 255), Color.fromARGB(255, 250, 250, 250)], // Muted Teal to Soft Lavender
                                            begin: Alignment.centerLeft,
                                            end: Alignment.centerRight,
                                          ),
                                          borderRadius: BorderRadius.circular(16),
                                        ),
                                        child: Text(
                                          '${5} new',
                                          style: const TextStyle(
                                            fontSize: 12,
                                            color: Color.fromARGB(255, 212, 0, 255), // Warm Grey
                                            fontWeight: FontWeight.w500,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                );
                              },
                            ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _toggleAddSiteWidget,
        backgroundColor: Provider.of<MetaController>(context).getThemeColor()[0],
        foregroundColor: Colors.white,
        elevation: 8,
        child: const Icon(
          Icons.add,
          size: 28,
        ),
      ),
    );
    });
  }
}