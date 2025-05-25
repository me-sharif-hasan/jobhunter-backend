// lib/features/jobs/ui/job_timeline_card.dart
import 'dart:developer';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/widgets/apply_button.dart';
import 'package:personalized_job_hunter/features/job/controller/job_timeline_controller.dart';
import 'package:provider/provider.dart';
import '../../common/domain/model/job_model.dart';
import '../../webview/inappwebview.dart';
import 'job_details_popup.dart';
import 'timeline_dot.dart';
import 'dart:io' show Platform;

class JobTimelineCard extends StatefulWidget {
  final Job job;
  final bool isFirst;
  final bool isLast;

  const JobTimelineCard({
    super.key,
    required this.job,
    this.isFirst = false,
    this.isLast = false,
  });

  @override
  State<JobTimelineCard> createState() => _JobTimelineCardState();
}

class _JobTimelineCardState extends State<JobTimelineCard> with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _scaleAnimation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: const Duration(milliseconds: 200),
      vsync: this,
    );
    _scaleAnimation = Tween<double>(begin: 1.0, end: 1.03).animate(
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
          if (widget.job.jobUrl != null) {
            showModalBottomSheet(
              context: context,
              isScrollControlled: true, // Allows dynamic height
              isDismissible: true,
              backgroundColor: Colors.transparent, // Gradient will handle background
              builder: (context) => DraggableScrollableSheet(
                initialChildSize: widget.job.jobDescription!=null&&widget.job.jobDescription!.isNotEmpty?0.5:0.26, // Start at 60% of screen height
                minChildSize: 0.1,
                maxChildSize: widget.job.jobDescription!=null&&widget.job.jobDescription!.isNotEmpty?0.7:0.4,
                builder: (context, scrollController) {
                  return SingleChildScrollView(
                    controller: scrollController,
                    child: JobDetailsPopup(job: widget.job),
                  );
                },
              ),
            );
          }
        },
        child: AnimatedBuilder(
          animation: _scaleAnimation,
          builder: (context, child) {
            return Transform.scale(
              scale: _scaleAnimation.value,
              child: child,
            );
          },
          child: IntrinsicHeight(
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                TimelineDot(isFirst: widget.isFirst, isLast: widget.isLast),
                Expanded(
                  child: Container(
                    margin: const EdgeInsets.symmetric(vertical: 12.0, horizontal: 18.0),
                    padding: const EdgeInsets.all(20.0), // Increased for more space
                    decoration: BoxDecoration(
                      gradient: const LinearGradient(
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                        colors: [
                          Color(0xFFFFA726), // Orange
                          Color(0xFFFF8E29), // Soft peach (toned down)
                        ],
                        stops: [0.0, 1.0],
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
                        Row(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            // Company logo (placeholder icon)
                            Container(
                              padding: const EdgeInsets.all(8.0),
                              decoration: BoxDecoration(
                                color: Colors.white.withOpacity(0.9),
                                shape: BoxShape.circle,
                              ),
                              child: widget.job.constructedIcon!=null?
                                  CachedNetworkImage(
                                    imageUrl: widget.job.constructedIcon!,
                                    width: 30,height: 30,
                                    fit: BoxFit.contain,
                                    errorWidget: (context, error, stackTrace) => const Icon(
                                      Icons.business,
                                      size: 30,
                                      color: Color(0xFFFFA726),
                                    )
                                  )
                                  :const Icon(
                                Icons.business,
                                size: 30,
                                color: Color(0xFFFFA726),
                              ),
                            ),
                            const SizedBox(width: 12.0),
                            // Title and Company
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    widget.job.title,
                                    style: Theme.of(context).textTheme.titleMedium?.copyWith(
                                      fontWeight: FontWeight.bold,
                                      color: Colors.white,
                                      fontSize: 18,
                                    ),
                                    maxLines: 2,
                                    overflow: TextOverflow.ellipsis,
                                  ),
                                  const SizedBox(height: 4),
                                  Text(
                                    widget.job.company ?? 'Unknown Company',
                                    style: Theme.of(context).textTheme.bodySmall?.copyWith(
                                      color: Colors.white.withOpacity(0.9),
                                      fontSize: 14,
                                    ),
                                    maxLines: 1,
                                    overflow: TextOverflow.ellipsis,
                                  ),
                                ],
                              ),
                            ),
                            // Apply button (top-right)
                            
                          ],
                        ),
                        const SizedBox(height: 12), // More spacing for height
                        // Metadata (Location, Date, Company Website)
                        Wrap(
                          spacing: 10.0,
                          runSpacing: 8.0,
                          children: [
                            _buildAttributeChip(
                              icon: Icons.location_on_outlined,
                              text: widget.job.location!=null&&widget.job.location!.isNotEmpty ? "${widget.job.location}" : 'Not specified',
                            ),
                            _buildAttributeChip(
                              icon: Icons.calendar_today_outlined,
                              text: widget.job.jobLastDate!=null&&widget.job.jobLastDate!.isNotEmpty? (widget.job.jobLastDate??"") : 'Unknown',
                            ),
                            _buildAttributeChip(
                                icon: Icons.history,
                                text: (widget.job.jobPostedDate!=null&&widget.job.jobPostedDate!.isNotEmpty)?widget.job.jobPostedDate??"":"Not Available"
                            ),
                            _buildAttributeChip(
                                icon: Icons.work,
                                text: (widget.job.experienceNeeded!=null&&widget.job.experienceNeeded!.isNotEmpty)?widget.job.experienceNeeded??"":"Not specified"
                            ),
                            if (widget.job.companyWebsite != null)
                              _buildWebsiteChip(
                                text: widget.job.companyWebsite!,
                                onTap: () => launchBrowser(widget.job.companyWebsite!),
                              ),
                            if ((widget.job.jobUrl??widget.job.jobApplyLink) != null)
                              Row(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                Flexible(
                                  child: ApplyButton(
                                    job: widget.job,
                                    buttonBorder:const BorderRadius.only(topLeft: Radius.circular(8),bottomLeft: Radius.circular(8)),
                                    icon: const Icon(Icons.arrow_forward,color: Colors.blue,),
                                    ),
                                ),
                                Flexible(
                                  child: ElevatedButton.icon(
                                    onPressed: (){
                                      log("Marking in progress");
                                      Provider.of<JobTimelineController>(context,listen: false).applyForJob(widget.job);
                                    },
                                    label: const Text("Applied"),
                                    icon: (widget.job.applied??false)?const Icon(Icons.check_circle,color: Colors.green,):const Icon(Icons.check_circle_outline,color: Colors.green,),
                                    style: ElevatedButton.styleFrom(
                                      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 8),
                                      backgroundColor: Colors.white,
                                      foregroundColor: Colors.green,
                                      shape: const RoundedRectangleBorder(
                                        borderRadius: BorderRadius.only(topRight: Radius.circular(8),bottomRight: Radius.circular(8)),
                                      ),
                                      elevation: 0,
                                      textStyle: const TextStyle(
                                        fontSize: 14,
                                        // fontWeight: FontWeight.w600,
                                      ),
                                    ).copyWith(
                                      overlayColor: MaterialStateProperty.all(
                                        const Color(0xFFFFA726).withOpacity(0.1),
                                      ),
                                    ),
                                    ),
                                ),
                                  // Flexible(
                                  //   child: ElevatedButton.icon(onPressed: (){},
                                  //     style: ElevatedButton.styleFrom(
                                  //       padding: const EdgeInsets.symmetric(horizontal: 5, vertical: 8),
                                  //       backgroundColor: Colors.white,
                                  //       foregroundColor: Colors.pink,
                                  //       shape: const RoundedRectangleBorder(
                                  //         borderRadius: BorderRadius.only(topRight: Radius.circular(8),bottomRight: Radius.circular(8)),
                                  //       ),
                                  //       elevation: 0,
                                  //       textStyle: const TextStyle(
                                  //         fontSize: 14,
                                  //         // fontWeight: FontWeight.w600,
                                  //       ),
                                  //     ).copyWith(
                                  //       overlayColor: MaterialStateProperty.all(
                                  //         const Color(0xFFFFA726).withOpacity(0.1),
                                  //       ),
                                  //     ),
                                  //     icon: const Icon(Icons.favorite_outline,color: Colors.pink,),
                                  //     label: const Text("Interested"),
                                  //   ),
                                  // )
                              ],)
                          ],
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildAttributeChip({required IconData icon, required String text}) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(6),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 16, color: Colors.white.withOpacity(0.9)),
          const SizedBox(width: 6),
          SizedBox(
            width: 250, // Fixed width to prevent overflow
            child: Text(
              text,
              style: Theme.of(context).textTheme.bodySmall?.copyWith(
                color: Colors.white.withOpacity(0.9),
                fontSize: 12,
              ),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildWebsiteChip({required String text, required VoidCallback onTap}) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
        decoration: BoxDecoration(
          color: const Color(0xFFFFA726).withOpacity(0.3),
          borderRadius: BorderRadius.circular(6),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Icons.link, size: 16, color: Colors.white.withOpacity(0.9)),
            const SizedBox(width: 6),
            SizedBox(
              width: 150, // Slightly wider for website URLs
              child: Text(
                text,
                style: Theme.of(context).textTheme.bodySmall?.copyWith(
                  color: Colors.white.withOpacity(0.9),
                  fontSize: 12,
                ),
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
      ),
    );
  }

  void launchBrowser(String url) {
    if(!kIsWeb&&(Platform.isAndroid || Platform.isIOS)){
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => JobWebViewScreen(job: widget.job,url: url,),
        ),
      );
    }else{
      if(kIsWeb){
        // js.context.callMethod('open', [url]);
      }else{
        // launchUrl(
        //     Uri.parse(url)
        // );
      }
    }

    print('Launching $url');
  }
}