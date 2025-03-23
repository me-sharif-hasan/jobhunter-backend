// lib/features/sites/ui/site_card.dart
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/webview/inappwebview.dart';
import 'package:personalized_job_hunter/util/time/time_util.dart';

import '../domain/models/site_model.dart';

class SiteCard extends StatefulWidget {
  final Site site;
  final Function(int) subscribe;
  final Function(int) unsubscribe;
  final bool isSubscribing;

  const SiteCard({
    super.key,
    required this.site,
    required this.subscribe,
    required this.unsubscribe,
    this.isSubscribing = false, // Default to true as per your code
  });

  @override
  State<SiteCard> createState() => _SiteCardState();
}

class _SiteCardState extends State<SiteCard>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _scaleAnimation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 200),
    );
    _scaleAnimation = Tween<double>(begin: 1.0, end: 1.02).animate(
      CurvedAnimation(parent: _controller, curve: Curves.easeInOut),
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MouseRegion(
      onEnter: (_) => _controller.forward(),
      onExit: (_) => _controller.reverse(),
      child: GestureDetector(
        onTap: () {
          // Optional: Add tap action if needed
        },
        child: AnimatedBuilder(
          animation: _controller,
          builder: (context, child) {
            return Transform.scale(
              scale: _scaleAnimation.value,
              child: Container(
                margin:
                    const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
                padding: const EdgeInsets.all(16.0),
                decoration: BoxDecoration(
                  gradient: const LinearGradient(
                    begin: Alignment.topLeft,
                    end: Alignment.bottomRight,
                    colors: [
                      Color(0xFFE94BFC), // Vibrant Purple
                      Color(0xFFFF7416), // Soft Peach
                    ],
                  ),
                  borderRadius: BorderRadius.circular(12),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.1),
                      blurRadius: 8,
                      offset: const Offset(0, 4),
                    ),
                  ],
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Header with Icon and Name
                    Row(
                      children: [
                        CircleAvatar(
                          radius: 24,
                          backgroundColor: Colors.white,
                          child: Padding(
                            padding: const EdgeInsets.all(10.0),
                            child: Image.network(
                              widget.site.constructedIcon,
                              width: 40,
                              height: 40,
                              fit: BoxFit.contain,
                              errorBuilder: (context, error, stackTrace) =>
                                  const Icon(Icons.business),
                            ),
                          ),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                widget.site.name,
                                style: Theme.of(context)
                                    .textTheme
                                    .titleMedium
                                    ?.copyWith(
                                      fontWeight: FontWeight.bold,
                                      color: Colors.white,
                                      fontSize: 18,
                                    ),
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                              ),
                              const SizedBox(height: 4),
                              Text(
                                widget.site.homepage,
                                style: TextStyle(
                                  color: Colors.white.withOpacity(0.8),
                                  fontSize: 14,
                                ),
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 12),
                    // Additional Details
                    Text(
                      'Last Crawled: ${toLocalTime(widget.site.lastCrawledAt)}',
                      style: TextStyle(
                        color: Colors.white.withOpacity(0.9),
                        fontSize: 14,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Job List Page: ${widget.site.jobListPageUrl}',
                      style: TextStyle(
                        color: Colors.white.withOpacity(0.9),
                        fontSize: 14,
                      ),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 12),
                    // Expandable Description
                    Theme(
                      data: Theme.of(context)
                          .copyWith(dividerColor: Colors.transparent),
                      child: ExpansionTile(
                        title: const Text(
                          'Description',
                          style: TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.w600,
                            fontSize: 14,
                          ),
                        ),
                        collapsedTextColor: Colors.white.withOpacity(0.9),
                        textColor: Colors.white,
                        iconColor: Colors.white.withOpacity(0.9),
                        collapsedIconColor: Colors.white.withOpacity(0.9),
                        childrenPadding:
                            const EdgeInsets.only(left: 16.0, bottom: 8.0),
                        children: [
                          Text(
                            widget.site.description,
                            style: TextStyle(
                              color: Colors.white.withOpacity(0.9),
                              fontSize: 14,
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 16),
                    // Button Group (Subscribe + Bell) and Visit Home Page
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        // Subscribe Button Group
                        Container(
                          decoration: BoxDecoration(
                            color: widget.site.subscribed
                                ? Colors.white.withOpacity(0.5)
                                : Colors.white.withOpacity(0.2),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Row(
                            children: [
                              TextButton(
                                onPressed: () {
                                  if (widget.isSubscribing) return;
                                  print('Subscribed to ${widget.site.name}');
                                  if (widget.site.subscribed) {
                                    widget.unsubscribe(widget.site.id);
                                  } else {
                                    widget.subscribe(widget.site.id);
                                  }
                                },
                                style: TextButton.styleFrom(
                                  padding: const EdgeInsets.symmetric(
                                      horizontal: 16, vertical: 12),
                                  foregroundColor: Colors.white,
                                  textStyle: const TextStyle(
                                    fontSize: 14,
                                    fontWeight: FontWeight.w600,
                                  ),
                                ),
                                child: Column(
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    Text(
                                      widget.site.subscribed
                                          ? 'Unsubscribe'
                                          : 'Subscribe',
                                    ),
                                    if (widget.isSubscribing) ...[
                                      const SizedBox(height: 4),
                                      SizedBox(
                                        width: 60, // Fixed width for the loader
                                        child: LinearProgressIndicator(
                                          backgroundColor:
                                              Colors.white.withOpacity(0.3),
                                          valueColor:
                                              const AlwaysStoppedAnimation<
                                                  Color>(Colors.white),
                                        ),
                                      ),
                                    ],
                                  ],
                                ),
                              ),
                              Container(
                                width: 1,
                                height: 24,
                                color: Colors.white
                                    .withOpacity(0.5), // Subtle divider
                              ),
                              IconButton(
                                onPressed: () {
                                  print(
                                      'Notifications toggled for ${widget.site.name}');
                                  // Add notification toggle logic here
                                },
                                icon: const Icon(Icons.notifications_outlined),
                                color: Colors.white,
                                padding:
                                    const EdgeInsets.symmetric(horizontal: 12),
                              ),
                            ],
                          ),
                        ),
                        // Visit Home Page Button
                        ElevatedButton(
                          onPressed: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => JobWebViewScreen.manual(
                                  url: widget.site.homepage,
                                  title: widget.site.name,
                                  company: widget.site.homepage,
                                ),
                              ),
                            );
                          },
                          style: ElevatedButton.styleFrom(
                            padding: const EdgeInsets.symmetric(
                                horizontal: 16, vertical: 12),
                            backgroundColor: const Color(0x1DFFFFFF),
                            // Slightly translucent white
                            foregroundColor: const Color(0xFFFFFFFF),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(8),
                            ),
                            elevation: 0,
                            textStyle: const TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.w600,
                            ),
                          ).copyWith(
                            overlayColor: MaterialStateProperty.all(
                              const Color(0xFFE94BFC).withOpacity(0.1),
                            ),
                          ),
                          child: const Text('Visit Home Page'),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}
