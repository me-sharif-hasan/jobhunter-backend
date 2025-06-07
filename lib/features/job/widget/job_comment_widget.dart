import 'dart:async';
import 'package:flutter/material.dart';
import 'package:loading_indicator/loading_indicator.dart';
import 'package:personalized_job_hunter/features/auth/domain/models/user_data_model.dart';
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
  int _lastCommentCount = 0; // Track comment list length to detect changes

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
  }

  void _startCommentSlideshow(List<JobCommentModel> comments) {
    if (comments.isNotEmpty && _timer == null && !_isDialogOpen) {
      _timer = Timer.periodic(const Duration(seconds: 4), (timer) {
        _animateComments();
      });
      _animateComments();
    }
  }

  void _stopCommentSlideshow() {
    _timer?.cancel();
    _timer = null;
  }

  void _animateComments() {
    if (mounted && !_isDialogOpen) {
      final comments = context.read<JobTimelineController>().jobCommentMap[widget.jobId] ?? [];
      if (comments.isNotEmpty) {
        setState(() {
          _currentCommentIndex = (_currentCommentIndex + 1) % comments.length;
        });
        _controller.reset();
        _controller.forward();
      } else {
        _stopCommentSlideshow();
      }
    }
  }

  void _pauseSlideshow() {
    _stopCommentSlideshow();
  }

  void _resumeSlideshow(List<JobCommentModel> comments) {
    if (comments.isNotEmpty && !_isDialogOpen) {
      _startCommentSlideshow(comments);
    }
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
      builder: (BuildContext context, JobTimelineController controller, Widget? child) {
        final comments = controller.jobCommentMap[widget.jobId] ?? [];
        // Detect comment list changes
        WidgetsBinding.instance.addPostFrameCallback((_) {
          if (comments.length != _lastCommentCount) {
            _lastCommentCount = comments.length;
            _stopCommentSlideshow();
            setState(() {
              // Reset index to avoid out-of-bounds when comment list changes
              _currentCommentIndex = comments.isNotEmpty ? 0 : 0;
            });
            _resumeSlideshow(comments);
          }
        });
        final lastComment = comments.isNotEmpty ? comments[_currentCommentIndex].comment : 'No comments yet';

        return GestureDetector(
          onTap: () {
            _pauseSlideshow();
            _isDialogOpen = true;
            _controller.forward();
            showDialog(
              context: context,
              barrierDismissible: true,
              builder: (context) {
                return AnimatedBuilder(
                  animation: _controller,
                  builder: (context, child) {
                    return Transform.scale(
                      scale: CurvedAnimation(
                        parent: _controller,
                        curve: Curves.easeOut,
                      ).value,
                      child: Dialog(
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(12),
                        ),
                        backgroundColor: const Color(0xFFFFFFFF),
                        child: Container(
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
                                        _controller.reverse();
                                        Navigator.pop(context);
                                      },
                                    ),
                                  ],
                                ),
                              ),
                              Expanded(
                                child: comments.isEmpty
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
                                  itemCount: comments.length,
                                  itemBuilder: (context, index) {
                                    final job = comments[index];
                                    return Padding(
                                      padding: const EdgeInsets.symmetric(vertical: 4.0),
                                      child: ListTile(
                                        contentPadding: const EdgeInsets.symmetric(horizontal: 8.0),
                                        leading: CircleAvatar(
                                          backgroundImage: NetworkImage(job.user.photoUrl ?? ""),
                                          radius: 20,
                                        ),
                                        title: Text(
                                          job.user.name,
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
                                              job.comment,
                                              style: const TextStyle(
                                                fontSize: 13,
                                                color: Color(0xFF757575),
                                              ),
                                              maxLines: 2,
                                              overflow: TextOverflow.ellipsis,
                                            ),
                                            const SizedBox(height: 2),
                                            Text(
                                              job.createTime.toLocal().toString().split('.')[0],
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
                                            controller.addComment(
                                              widget.jobId,
                                              JobCommentModel(
                                                user: UserDataModel(
                                                  id: "current_user", // Replace with actual user data
                                                  name: "Current User", // Replace with actual user data
                                                  email: "user@example.com", // Replace with actual user data
                                                  photoUrl: "https://randomuser.me/api/portraits/men/1.jpg", // Replace with actual user data
                                                ),
                                                uuid: DateTime.now().millisecondsSinceEpoch.toString(),
                                                comment: _commentController.text,
                                                jobId: widget.jobId,
                                                userId: 1, // Replace with actual user ID
                                                createTime: DateTime.now(),
                                                updateTime: DateTime.now(),
                                              ),
                                            );
                                            _commentController.clear();
                                            ScaffoldMessenger.of(context).showSnackBar(
                                              SnackBar(
                                                content: Text('Comment posted: ${_commentController.text}'),
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
                        ),
                      ),
                    );
                  },
                );
              },
            ).then((_) {
              _controller.reverse();
              _isDialogOpen = false;
              _resumeSlideshow(comments);
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
                controller.jobCommentMap[widget.jobId]==null?const LoadingIndicator(
                    indicatorType: Indicator.ballPulse, /// Required, The loading type of the widget
                    colors: [Colors.purple,Colors.blue,Colors.orange],       /// Optional, The color collections
                    strokeWidth: 2,                     /// Optional, The stroke of the line, only applicable to widget which contains line
                    backgroundColor: Colors.transparent,      /// Optional, Background of the widget
                    pathBackgroundColor: Colors.black   /// Optional, the stroke backgroundColor
                ):Container(
                  padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                  decoration: BoxDecoration(
                    color: const Color(0xFFFF6200),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: const Text(
                    'Comments',
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 10,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                const SizedBox(width: 4),
                Expanded(
                  child: comments.isNotEmpty
                      ? ClipRect(
                    child: AnimatedBuilder(
                      animation: _controller, // Tie animation to controller
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
                              key: ValueKey(lastComment), // Ensure rebuild on comment change
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