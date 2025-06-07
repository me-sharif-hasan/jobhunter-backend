import 'dart:async';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/auth/domain/models/user_data_model.dart';

class JobCommentWidget extends StatefulWidget {
  const JobCommentWidget({super.key});

  @override
  State<JobCommentWidget> createState() => _JobCommentWidgetState();
}

class _JobCommentWidgetState extends State<JobCommentWidget> with SingleTickerProviderStateMixin {
  final List<Map<String, dynamic>> _dummyJobs = [
    {
      "user": UserDataModel(
        id: "52",
        name: "Sharif Hasan",
        email: "sharif.hasan@example.com",
        photoUrl: "https://randomuser.me/api/portraits/men/52.jpg",
      ),
      "uuid": "11db45cc-02ae-4c6f-8533-e7f6f8060ce0",
      "comment": "This Laravel developer role looks exciting! Any insights on the team culture?",
      "jobId": "https___6amtech_com_career__job_laravel_developer",
      "userId": 52,
      "parentUuid": "8848e22f-dc59-49a5-9cce-04431a0b224c",
      "createTime": 1749276242926,
      "updateTime": 1749276242926
    },
    {
      "user": UserDataModel(
        id: "78",
        name: "Ayesha Khan",
        email: "ayesha.khan@example.com",
        photoUrl: "https://randomuser.me/api/portraits/women/78.jpg",
      ),
      "uuid": "8848e22f-dc59-49a5-9cce-04431a0b224c",
      "comment": "Great opportunity! Is remote work available for this position?",
      "jobId": "https___6amtech_com_career__job_laravel_developer",
      "userId": 78,
      "parentUuid": null,
      "createTime": 1749276095980,
      "updateTime": 1749276095980
    },
    {
      "user": UserDataModel(
        id: "19",
        name: "Rahul Sharma",
        email: "rahul.sharma@example.com",
        photoUrl: "https://randomuser.me/api/portraits/men/19.jpg",
      ),
      "uuid": "0c86f568-ccb5-43a2-a1e9-85575f69c480",
      "comment": "Just applied! Any tips for the interview process?",
      "jobId": "https___6amtech_com_career__job_laravel_developer",
      "userId": 19,
      "parentUuid": null,
      "createTime": 1749276203126,
      "updateTime": 1749276203126
    },
    {
      "user": UserDataModel(
        id: "19",
        name: "Rahul Sharma",
        email: "rahul.sharma@example.com",
        photoUrl: "https://randomuser.me/api/portraits/men/19.jpg",
      ),
      "uuid": "0c86f568-ccb5-43a2-a1e9-85575f69c480",
      "comment": "Just applied! Any tips for the interview process?",
      "jobId": "https___6amtech_com_career__job_laravel_developer",
      "userId": 19,
      "parentUuid": null,
      "createTime": 1749276203126,
      "updateTime": 1749276203126
    },
    {
      "user": UserDataModel(
        id: "19",
        name: "Rahul Sharma",
        email: "rahul.sharma@example.com",
        photoUrl: "https://randomuser.me/api/portraits/men/19.jpg",
      ),
      "uuid": "0c86f568-ccb5-43a2-a1e9-85575f69c480",
      "comment": "Just applied! Any tips for the interview process?",
      "jobId": "https___6amtech_com_career__job_laravel_developer",
      "userId": 19,
      "parentUuid": null,
      "createTime": 1749276203126,
      "updateTime": 1749276203126
    },
    {
      "user": UserDataModel(
        id: "19",
        name: "Rahul Sharma",
        email: "rahul.sharma@example.com",
        photoUrl: "https://randomuser.me/api/portraits/men/19.jpg",
      ),
      "uuid": "0c86f568-ccb5-43a2-a1e9-85575f69c480",
      "comment": "Just applied! Any tips for the interview process?",
      "jobId": "https___6amtech_com_career__job_laravel_developer",
      "userId": 19,
      "parentUuid": null,
      "createTime": 1749276203126,
      "updateTime": 1749276203126
    },
  ];

  late AnimationController _controller;
  late Animation<Offset> _slideAnimation;
  final TextEditingController _commentController = TextEditingController();
  int _currentCommentIndex = 0;
  late Timer _timer;
  bool _isDialogOpen = false; // Track dialog state

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

    if (_dummyJobs.isNotEmpty) {
      _startCommentSlideshow();
    }
  }

  void _animateComments() {
    if (mounted && !_isDialogOpen) { // Only animate if dialog is not open
      setState(() {
        _currentCommentIndex = (_currentCommentIndex + 1) % _dummyJobs.length;
      });
      _controller.reset();
      _controller.forward();
    }
  }

  void _startCommentSlideshow() {
    _animateComments();
    _timer = Timer.periodic(const Duration(seconds: 4), (timer) {
      _animateComments();
    });
  }

  void _pauseSlideshow() {
    _timer.cancel(); // Pause the timer
  }

  void _resumeSlideshow() {
    if (_dummyJobs.isNotEmpty && !_isDialogOpen) {
      _startCommentSlideshow(); // Restart the timer
    }
  }

  @override
  void dispose() {
    _timer.cancel();
    _controller.dispose();
    _commentController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final lastComment = _dummyJobs.isNotEmpty ? _dummyJobs[_currentCommentIndex]['comment'] : 'No comments yet';

    return GestureDetector(
      onTap: () {
        _pauseSlideshow(); // Pause slideshow when dialog opens
        _isDialogOpen = true; // Mark dialog as open
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
                    backgroundColor: const Color(0xFFFFFFFF), // MD3 Surface
                    child: Container(
                      width: MediaQuery.of(context).size.width * 0.8,
                      constraints: BoxConstraints(
                        maxHeight: MediaQuery.of(context).size.height * 0.6,
                      ),
                      child: Column(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          // Header
                          Padding(
                            padding: const EdgeInsets.symmetric(horizontal: 8.0, vertical: 12.0),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                const Row(
                                  children: [
                                    Icon(Icons.voice_chat,color: Color(0xFF272727),),
                                    SizedBox(width: 10,),
                                    Text(
                                      'Share your thoughts',
                                      style: TextStyle(
                                        fontSize: 16,
                                        fontWeight: FontWeight.bold,
                                        color: Color(0xFF272727), // MD3 primary
                                      ),
                                    )
                                  ],
                                ),
                                IconButton(
                                  icon: const Icon(
                                    Icons.close,
                                    color: Color(0xFF757575), // MD3 onSurfaceVariant
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
                          // Comment List
                          Expanded(
                            child: _dummyJobs.isEmpty
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
                              itemCount: _dummyJobs.length,
                              itemBuilder: (context, index) {
                                final job = _dummyJobs[index];
                                return Padding(
                                  padding: const EdgeInsets.symmetric(vertical: 4.0),
                                  child: ListTile(
                                    contentPadding: const EdgeInsets.symmetric(horizontal: 8.0),
                                    leading: CircleAvatar(
                                      backgroundImage: NetworkImage(job['user'].photoUrl),
                                      radius: 20, // Compact avatar
                                    ),
                                    title: Text(
                                      job['user'].name,
                                      style: const TextStyle(
                                        fontSize: 13,
                                        fontWeight: FontWeight.w600,
                                        color: Color(0xFF000000), // MD3 onSurface
                                      ),
                                    ),
                                    subtitle: Column(
                                      crossAxisAlignment: CrossAxisAlignment.start,
                                      children: [
                                        Text(
                                          job['comment'],
                                          style: const TextStyle(
                                            fontSize: 13,
                                            color: Color(0xFF757575), // MD3 onSurfaceVariant
                                          ),
                                          maxLines: 2,
                                          overflow: TextOverflow.ellipsis,
                                        ),
                                        const SizedBox(height: 2),
                                        Text(
                                          DateTime.fromMillisecondsSinceEpoch(job['createTime'])
                                              .toLocal()
                                              .toString()
                                              .split('.')[0],
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
                          // Post Comment
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Container(
                              height: 36,
                              padding: const EdgeInsets.symmetric(horizontal: 6),
                              decoration: BoxDecoration(
                                color: const Color(0xFFF5F5F5), // MD3 Surface
                                borderRadius: BorderRadius.circular(20), // Pill-shaped
                              ),
                              child: Row(
                                crossAxisAlignment: CrossAxisAlignment.center, // Ensure vertical centering
                                children: [
                                  Expanded(
                                    child: TextField(
                                      controller: _commentController,
                                      textAlignVertical: TextAlignVertical.center, // Center text vertically
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
                                        contentPadding: EdgeInsets.symmetric(horizontal: 8, vertical: 10), // Adjusted for centering
                                      ),
                                    ),
                                  ),
                                  GestureDetector(
                                    onTap: () {
                                      if (_commentController.text.isNotEmpty) {
                                        // Placeholder for posting comment
                                        ScaffoldMessenger.of(context).showSnackBar(
                                          SnackBar(
                                            content: Text('Comment: ${_commentController.text}'),
                                          ),
                                        );
                                        _commentController.clear();
                                      }
                                    },
                                    child: const Icon(
                                      Icons.send,
                                      color: Color(0xFFD53FFF), // MD3 primary
                                      size: 16,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          )
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
          _isDialogOpen = false; // Mark dialog as closed as dialog is disposed
          _resumeSlideshow(); // Resume slideshow after dialog closes
        });
      },
      child: Container(
        height: 36, // Matches ModernSearchBar
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
        decoration: BoxDecoration(
          color: const Color(0xFFFFFFFF), // Flat, subtle opacity
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
            Container(
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
              child: ClipRect(
                child: AnimatedBuilder(
                  animation: _slideAnimation,
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
                          style: const TextStyle(
                            fontSize: 13,
                            fontWeight: FontWeight.w400,
                            color: Colors.white, // Base for gradient
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                    );
                  },
                ),
              ),
            ),
            const Icon(
              Icons.send,
              color: Color(0xFF6200EA), // MD3 primary
              size: 16,
            ),
          ],
        ),
      ),
    );
  }
}