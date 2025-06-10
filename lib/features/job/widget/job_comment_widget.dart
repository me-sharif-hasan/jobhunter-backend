import 'dart:async';
import 'package:flutter/material.dart';
import 'package:loading_indicator/loading_indicator.dart';
import 'package:personalized_job_hunter/features/job/controller/job_timeline_controller.dart';
import 'package:personalized_job_hunter/features/job/domain/model/job_comment_model.dart';
import 'package:provider/provider.dart';

class JobCommentWidget extends StatefulWidget {
  final String jobId;
  const JobCommentWidget({super.key, required this.jobId});

  @override
  State<JobCommentWidget> createState() => _JobCommentWidgetState();
}

class _JobCommentWidgetState extends State<JobCommentWidget> with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<Offset> _slideAnimation;
  final TextEditingController _commentController = TextEditingController();
  int _currentCommentIndex = 0;
  Timer? _timer;
  bool _isDialogOpen = false;
  bool _isCommentsLoaded = false;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: const Duration(milliseconds: 500),
      vsync: this,
    );
    _slideAnimation = Tween<Offset>(
      begin: const Offset(1.0, 0.0),
      end: Offset.zero,
    ).animate(CurvedAnimation(
      parent: _controller,
      curve: Curves.easeInOut,
    ));

    // Load comments initially
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<JobTimelineController>().loadComments(widget.jobId).then((_) {
        if (mounted) {
          setState(() {
            _isCommentsLoaded = true;
          });
          final comments = context.read<JobTimelineController>().jobCommentMap[widget.jobId] ?? [];
          if (comments.isNotEmpty) {
            _startCommentSlideshow(comments);
          }
        }
      });
    });
  }

  void _startCommentSlideshow(List<JobCommentModel> comments) {
    if (comments.isNotEmpty && _timer == null && !_isDialogOpen && mounted) {
      _timer = Timer.periodic(const Duration(seconds: 4), (timer) {
        if (mounted && !_isDialogOpen && comments.isNotEmpty) {
          setState(() {
            _currentCommentIndex = (_currentCommentIndex + 1) % comments.length;
          });
          _controller.reset();
          _controller.forward();
        } else {
          _stopCommentSlideshow();
        }
      });
      // Trigger initial animation
      if (mounted) {
        setState(() {
          _currentCommentIndex = 0;
        });
        _controller.reset();
        _controller.forward();
      }
    }
  }

  void _stopCommentSlideshow() {
    _timer?.cancel();
    _timer = null;
  }

  @override
  void dispose() {
    _stopCommentSlideshow();
    _controller.dispose();
    _commentController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<JobTimelineController>(
      builder: (context, controller, child) {
        final comments = controller.jobCommentMap[widget.jobId] ?? [];
        final lastComment = comments.isNotEmpty && _isCommentsLoaded
            ? comments[_currentCommentIndex].comment
            : 'No comments yet';

        return GestureDetector(
          onTap: () {
            _stopCommentSlideshow();
            _isDialogOpen = true;
            _controller.reset(); // Reset controller for dialog animation
            _controller.forward(); // Start dialog animation
            showDialog(
              context: context,
              barrierDismissible: true,
              builder: (dialogContext) {
                return Dialog(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  backgroundColor: const Color(0xFFFFFFFF),
                  child: Consumer<JobTimelineController>(
                    builder: (context, controller, child) {
                      final dialogComments = controller.jobCommentMap[widget.jobId] ?? [];
                      return Container(
                        width: MediaQuery.of(context).size.width * 0.8,
                        constraints: BoxConstraints(
                          maxHeight: MediaQuery.of(context).size.height * 0.6,
                        ),
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 8.0, vertical: 12.0),
                              child: Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: [
                                  const Row(
                                    children: [
                                      Icon(
                                        Icons.voice_chat,
                                        color: Color(0xFF272727),
                                      ),
                                      SizedBox(width: 10),
                                      Text(
                                        'Share your thoughts',
                                        style: TextStyle(
                                          fontSize: 16,
                                          fontWeight: FontWeight.bold,
                                          color: Color(0xFF272727),
                                        ),
                                      ),
                                    ],
                                  ),
                                  IconButton(
                                    icon: const Icon(
                                      Icons.close,
                                      color: Color(0xFF757575),
                                      size: 20,
                                    ),
                                    onPressed: () {
                                      _controller.reverse().then((_) {
                                        Navigator.pop(dialogContext);
                                      });
                                    },
                                  ),
                                ],
                              ),
                            ),
                            Expanded(
                              child: dialogComments.isEmpty
                                  ? const Center(
                                child: Text(
                                  'No comments yet',
                                  style: TextStyle(
                                    color: Color(0xFF757575),
                                    fontSize: 13,
                                  ),
                                ),
                              )
                                  : ListView.builder(
                                padding: const EdgeInsets.symmetric(horizontal: 8.0),
                                itemCount: dialogComments.length,
                                itemBuilder: (context, index) {
                                  final comment = dialogComments[index];
                                  return Padding(
                                    padding: const EdgeInsets.symmetric(vertical: 4.0),
                                    child: ListTile(
                                      contentPadding: const EdgeInsets.symmetric(horizontal: 8.0),
                                      leading: CircleAvatar(
                                        backgroundImage: NetworkImage(comment.user.photoUrl ?? ""),
                                        radius: 20,
                                      ),
                                      title: Text(
                                        comment.user.name,
                                        style: const TextStyle(
                                          fontSize: 13,
                                          fontWeight: FontWeight.w600,
                                          color: Color(0xFF000000),
                                        ),
                                      ),
                                      subtitle: Column(
                                        crossAxisAlignment: CrossAxisAlignment.start,
                                        children: [
                                          Text(
                                            comment.comment,
                                            style: const TextStyle(
                                              fontSize: 13,
                                              color: Color(0xFF757575),
                                            ),
                                            maxLines: 2,
                                            overflow: TextOverflow.ellipsis,
                                          ),
                                          const SizedBox(height: 2),
                                          Text(
                                            comment.createTime.toLocal().toString().split('.')[0],
                                            style: const TextStyle(
                                              fontSize: 11,
                                              color: Color(0xFF757575),
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  );
                                },
                              ),
                            ),
                            Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Container(
                                height: 36,
                                padding: const EdgeInsets.symmetric(horizontal: 6),
                                decoration: BoxDecoration(
                                  color: const Color(0xFFF5F5F5),
                                  borderRadius: BorderRadius.circular(20),
                                ),
                                child: Row(
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    Expanded(
                                      child: TextField(
                                        controller: _commentController,
                                        textAlignVertical: TextAlignVertical.center,
                                        style: const TextStyle(
                                          fontSize: 13,
                                          color: Color(0xFF757575),
                                        ),
                                        decoration: const InputDecoration(
                                          hintText: 'Add a comment...',
                                          hintStyle: TextStyle(
                                            color: Color(0xFF757575),
                                            fontSize: 13,
                                          ),
                                          border: InputBorder.none,
                                          contentPadding: EdgeInsets.symmetric(horizontal: 8, vertical: 10),
                                        ),
                                      ),
                                    ),
                                    GestureDetector(
                                      onTap: () {
                                        if (_commentController.text.isNotEmpty) {
                                          controller.addComment(widget.jobId, _commentController.text);
                                          _commentController.clear();
                                          ScaffoldMessenger.of(context).showSnackBar(
                                            const SnackBar(
                                              content: Text('Comment posted'),
                                            ),
                                          );
                                        }
                                      },
                                      child: const Icon(
                                        Icons.send,
                                        color: Color(0xFFD53FFF),
                                        size: 16,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          ],
                        ),
                      );
                    },
                  ),
                );
              },
            ).then((_) {
              _isDialogOpen = false;
              final comments = controller.jobCommentMap[widget.jobId] ?? [];
              if (comments.isNotEmpty && mounted) {
                _startCommentSlideshow(comments);
              }
            });
          },
          child: Container(
            height: 36,
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
            decoration: BoxDecoration(
              color: const Color(0xFFFFFFFF),
              borderRadius: BorderRadius.circular(5),
              boxShadow: const [
                BoxShadow(
                  color: Color(0x11000000),
                  offset: Offset(5.0, 5.0),
                  blurRadius: 10.0,
                  spreadRadius: 2.0,
                ),
              ],
            ),
            child: Row(
              children: [
                const SizedBox(width: 4),
                controller.jobCommentMap[widget.jobId] == null && !_isCommentsLoaded
                    ? const LoadingIndicator(
                  indicatorType: Indicator.ballPulse,
                  colors: [Colors.purple, Colors.blue, Colors.orange],
                  strokeWidth: 2,
                  backgroundColor: Colors.transparent,
                  pathBackgroundColor: Colors.black,
                )
                    : Container(
                  padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                  decoration: BoxDecoration(
                    color: const Color(0xFFFF6200),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: const Text(
                    'Comments',
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 10,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                const SizedBox(width: 4),
                Expanded(
                  child: _isCommentsLoaded && comments.isNotEmpty
                      ? ClipRect(
                    child: AnimatedBuilder(
                      animation: _controller,
                      builder: (context, child) {
                        return SlideTransition(
                          position: _slideAnimation,
                          child: ShaderMask(
                            shaderCallback: (bounds) => const LinearGradient(
                              colors: [Color(0xFF232323), Color(0xFF0C0B0B)],
                              begin: Alignment.centerLeft,
                              end: Alignment.centerRight,
                            ).createShader(bounds),
                            child: Text(
                              lastComment,
                              key: ValueKey(lastComment),
                              style: const TextStyle(
                                fontSize: 13,
                                fontWeight: FontWeight.w400,
                                color: Colors.white,
                              ),
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                            ),
                          ),
                        );
                      },
                    ),
                  )
                      : const Padding(
                    padding: EdgeInsets.symmetric(horizontal: 16),
                    child: Text("Share your thoughts"),
                  ),
                ),
                const Icon(
                  Icons.send,
                  color: Color(0xFF6200EA),
                  size: 16,
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}