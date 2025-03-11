import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/auth/controller/user_login_controller.dart';
import 'package:personalized_job_hunter/features/auth/screens/login_screen.dart';
import 'package:personalized_job_hunter/features/common/screens/main_page.dart';
import 'package:provider/provider.dart';

import '../../auth/controller/auth_controller.dart';
import '../../auth/screens/signin_with_google_screen.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    Provider.of<AuthController>(context, listen: false).getCurrentUser();
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    return Consumer<AuthController>(
      builder: (context, controller, _) {
        return getScreen(controller);
      },
    );
  }


  Widget getScreen(AuthController controller){
    log('Status: ${controller.status}');
    switch(controller.status){
      case AuthStatus.authenticated:
        return const MainScreen();
      case AuthStatus.unauthenticated:
        return const SignInWithGoogleScreen();
      default:
        return Scaffold(
          body: Container(
            decoration: const BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
                colors: [
                  Color(0xFFFFA726), // Orange (hopeful start)
                  Color(0xFFFF963C), // Light Orange (hopeful finish)
                ],
              ),
            ),
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  // Logo
                  Image.asset(
                    'assets/logo.png',
                    width: 200, // Adjust size as needed
                    height: 200,
                  ),
                  const SizedBox(height: 32), // Spacing between logo and loader
                  // Customized Circular Progress Indicator
                  CircularProgressIndicator(
                    valueColor: const AlwaysStoppedAnimation<Color>(Colors.white),
                    backgroundColor: Colors.white.withOpacity(0.3),
                    strokeWidth: 6.0, // Thicker for visibility
                  ),
                ],
              ),
            ),
          ),
        );
    }
  }
}