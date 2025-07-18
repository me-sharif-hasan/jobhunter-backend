import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/auth/controller/auth_controller.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';
import 'package:provider/provider.dart';

import '../widget/resume_upload_button.dart';

class UserProfileScreen extends StatefulWidget {
  const UserProfileScreen({super.key});

  @override
  State<UserProfileScreen> createState() => _UserProfileScreenState();
}

class _UserProfileScreenState extends State<UserProfileScreen> {
  @override
  void initState() {
    Provider.of<AuthController>(context, listen: false).getCurrentUser();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<AuthController>(
      builder: (context, controller, _) {
        return Scaffold(
          backgroundColor: Colors.transparent, // Transparent to show gradient
          body: Container(
            decoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topCenter,
                end: Alignment.bottomCenter,
                colors: [
                  Constants.getThemeColor(0)[0],
                  Constants.getThemeColor(0)[1],
                ],
              ),
            ),
            child: Stack(
              children: [
                // Main content
                SafeArea(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(
                        horizontal: 24.0, vertical: 32.0),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Stack(
                          alignment: Alignment.bottomRight,
                          children: [
                            CircleAvatar(
                              radius: 70,
                              backgroundImage:
                                  controller.userDataModel?.photoUrl != null
                                      ? NetworkImage(
                                          controller.userDataModel!.photoUrl!)
                                      : null,
                              child: controller.userDataModel?.photoUrl == null
                                  ? const Icon(
                                      Icons.person,
                                      size: 70,
                                      color: Colors.white,
                                    )
                                  : null,
                            ),
                            Positioned(
                              bottom: 0,
                              right: 0,
                              child: CircleAvatar(
                                radius: 20,
                                backgroundColor: Colors.white.withOpacity(0.9),
                                child: IconButton(
                                  icon: const Icon(Icons.camera_alt,
                                      color: Colors.black54),
                                  onPressed: () {
                                    print('Change profile picture tapped');
                                    // Implement image picker logic here
                                  },
                                ),
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 32),
                        // Name with edit option
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Text(
                              controller.userDataModel?.name ?? 'Unknown',
                              style: const TextStyle(
                                fontSize: 28,
                                fontWeight: FontWeight.bold,
                                color: Colors.white,
                              ),
                            ),
                            const SizedBox(width: 8),
                            IconButton(
                              icon: const Icon(Icons.edit, color: Colors.white),
                              onPressed: () {
                                final TextEditingController textController =
                                    TextEditingController(
                                        text: controller.userDataModel?.name ??
                                            '');
                                showDialog(
                                  context: context,
                                  builder: (context) => AlertDialog(
                                    title: const Text('Edit Name'),
                                    content: TextField(
                                      controller: textController,
                                      decoration: const InputDecoration(
                                        hintText: 'Enter new name',
                                      ),
                                    ),
                                    actions: [
                                      TextButton(
                                        onPressed: () => Navigator.pop(context),
                                        child: const Text('Cancel'),
                                      ),
                                      TextButton(
                                        onPressed: () {
                                          print(
                                              'New name: ${textController.text}');
                                          // Update name in AuthController or backend
                                          Navigator.pop(context);
                                        },
                                        child: const Text('Save'),
                                      ),
                                    ],
                                  ),
                                );
                              },
                            ),
                          ],
                        ),
                        const SizedBox(height: 16),
                        // Email (non-editable)
                        Text(
                          controller.userDataModel?.email ?? 'No email',
                          style: const TextStyle(
                            fontSize: 18,
                            color: Colors.white70,
                          ),
                        ),
                        Column(children: [
                          const SizedBox(height: 32),
                          const ResumeUploadButton(),
                          const SizedBox(height: 16),
                          // FacebookButtonWidget(onPressed: () {
                          //   print('Connect with Facebook tapped');
                          //   // Implement Facebook login logic here
                          // }),
                        ])
                      ],
                    ),
                  ),
                ),
                // Logout button positioned manually at top-right
                Positioned(
                  top: 40,
                  right: 16,
                  child: IconButton(
                    icon:
                        const Icon(Icons.logout, color: Colors.white, size: 28),
                    onPressed: () {
                      showDialog(
                        context: context,
                        builder: (context) => AlertDialog(
                          title: const Text('Leaving So Soon?'),
                          content: const Text(
                            'Logging out will stop you from further notification in app. But email notification continues working!',
                            style: TextStyle(color: Colors.black87),
                          ),
                          actions: [
                            TextButton(
                              onPressed: () => Navigator.pop(context),
                              child: const Text('Stay'),
                            ),
                            TextButton(
                              onPressed: () {
                                Navigator.pop(context);
                                // Logout logic here (e.g., call controller.logout())
                                print('User logged out');
                                // Example: Navigate to sign-in screen
                                // Navigator.pushReplacementNamed(context, '/signing');
                                controller.logout();
                              },
                              child: const Text(
                                'Logout',
                                style: TextStyle(color: Colors.red),
                              ),
                            ),
                          ],
                        ),
                      );
                    },
                    tooltip: 'Logout',
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
