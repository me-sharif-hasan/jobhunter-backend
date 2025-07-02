import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/job/controller/job_timeline_controller.dart';
import 'package:personalized_job_hunter/features/job/widget/job_comment_widget.dart';
import 'package:provider/provider.dart';
import '../../common/domain/model/job_model.dart';
import 'job_details_popup.dart';
import 'timeline_dot.dart';
import 'company_logo.dart';
import 'job_title_card.dart';
import 'job_metadata_chips.dart';
import 'apply_status_banner.dart';
import 'apply_action_buttons.dart';

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

class _JobTimelineCardState extends State<JobTimelineCard>
    with SingleTickerProviderStateMixin {
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
              isScrollControlled: true,
              isDismissible: true,
              backgroundColor: Colors.transparent,
              builder: (context) => DraggableScrollableSheet(
                initialChildSize: widget.job.jobDescription != null &&
                    widget.job.jobDescription!.isNotEmpty
                    ? 0.5
                    : 0.26,
                minChildSize: 0.1,
                maxChildSize: widget.job.jobDescription != null &&
                    widget.job.jobDescription!.isNotEmpty
                    ? 0.7
                    : 0.4,
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
          child: Wrap(
            children: [
              IntrinsicHeight(
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    TimelineDot(isFirst: widget.isFirst, isLast: widget.isLast),
                    Expanded(
                      child: Container(
                        margin: const EdgeInsets.symmetric(
                            vertical: 12.0, horizontal: 18.0),
                        padding: const EdgeInsets.all(10.0),
                        decoration: BoxDecoration(
                          gradient: const LinearGradient(
                            begin: Alignment.topLeft,
                            end: Alignment.bottomRight,
                            colors: [
                              Color(0xFFFFA726),
                              Color(0xFFFF8E29),
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
                                CompanyLogo(job: widget.job),
                                const SizedBox(width: 12.0),
                                JobTitleCard(job: widget.job),
                                IconButton(
                                  onPressed: () {
                                    Provider.of<JobTimelineController>(context, listen: false)
                                        .toggleFavorite(widget.job);
                                  },
                                  icon: const Icon(
                                    Icons.insights,
                                    color: Color.fromARGB(255, 255, 255, 255), // Dark color for contrast
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: 12),
                            JobMetadataChips(job: widget.job),
                            const SizedBox(height: 12),
                            Container(
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(8),
                                color: Colors.white,
                                boxShadow: [
                                  BoxShadow(
                                    color: Colors.black.withOpacity(0.1),
                                    blurRadius: 4,
                                    offset: const Offset(0, 2),
                                  ),
                                ],
                              ),
                              child: Column(
                                children: [
                                  ApplyActionButtons(job: widget.job),
                                  JobCommentWidget(jobId: widget.job.jobId,),
                            ApplyStatusBanner(job: widget.job),
                                ],
                              ),
                            )
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}