import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:personalized_job_hunter/features/common/widgets/search_bar.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';
import 'package:provider/provider.dart';

class PersonalJobWatcherScreen extends StatefulWidget {
  const PersonalJobWatcherScreen({super.key});

  @override
  State<PersonalJobWatcherScreen> createState() => _PersonalJobWatcherScreenState();
}

class _PersonalJobWatcherScreenState extends State<PersonalJobWatcherScreen> with SingleTickerProviderStateMixin {
  final TextEditingController _urlController = TextEditingController();
  late AnimationController _controller;
  late Animation<double> _animation;

  // Dummy data for sites
  final List<Map<String, dynamic>> _dummySites = [
    {
      'title': 'TechBD Careers',
      'logoUrl': 'https://via.placeholder.com/40',
      'lastCrawled': DateTime(2025, 6, 20, 14, 30),
      'jobCount': 5,
    },
    {
      'title': 'BDJobs Careers',
      'logoUrl': 'https://via.placeholder.com/40',
      'lastCrawled': DateTime(2025, 6, 19, 10, 15),
      'jobCount': 12,
    },
  ];

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: const Duration(milliseconds: 200),
      vsync: this,
    );
    _animation = CurvedAnimation(parent: _controller, curve: Curves.easeInOut);
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

  @override
  Widget build(BuildContext context) {
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
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          children: [
            // Input Section
            ModernSearchBar(onSearch: (String query){}),
            const SizedBox(height: 12),
            // Site List
            Expanded(
              child: _dummySites.isEmpty
                  ? const Center(
                      child: Text(
                        'No sites added yet',
                        style: TextStyle(
                          color: Color(0xFFCE93D8), // Soft Lavender
                          fontSize: 14,
                        ),
                      ),
                    )
                  : ListView.builder(
                      itemCount: _dummySites.length,
                      itemBuilder: (context, index) {
                        final site = _dummySites[index];
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
                              Container(
                                width: 40,
                                height: 40,
                                decoration: BoxDecoration(
                                  shape: BoxShape.circle,
                                  color: Color(0xFFCFD8DC).withOpacity(0.2), // Warm Grey
                                ),
                                padding: const EdgeInsets.all(8),
                                child: ClipOval(
                                  child: CachedNetworkImage(
                                    imageUrl: site['logoUrl'],
                                    fit: BoxFit.cover,
                                    errorWidget: (context, url, error) =>
                                        const Icon(Icons.web, color: Color(0xFFCFD8DC)), // Warm Grey
                                  ),
                                ),
                              ),
                              const SizedBox(width: 12),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      site['title'],
                                      style: const TextStyle(
                                        fontSize: 16,
                                        fontWeight: FontWeight.w600,
                                        color: Color(0xFFCFD8DC), // Warm Grey
                                      ),
                                    ),
                                    const SizedBox(height: 4),
                                    Text(
                                      'Last crawled: ${_formatTimestamp(site['lastCrawled'])}',
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
                                    colors: [Color(0xFF4DB6AC), Color(0xFFCE93D8)], // Muted Teal to Soft Lavender
                                    begin: Alignment.centerLeft,
                                    end: Alignment.centerRight,
                                  ),
                                  borderRadius: BorderRadius.circular(16),
                                ),
                                child: Text(
                                  '${site['jobCount']} jobs',
                                  style: const TextStyle(
                                    fontSize: 12,
                                    color: Color(0xFFCFD8DC), // Warm Grey
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
          ],
        ),
      ),
    );
  }
}