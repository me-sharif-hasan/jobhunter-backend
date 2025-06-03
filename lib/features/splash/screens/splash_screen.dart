import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:personalized_job_hunter/features/auth/controller/auth_controller.dart';
import 'package:personalized_job_hunter/features/auth/screens/signin_with_google_screen.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:personalized_job_hunter/features/common/screens/main_page.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';
import 'package:provider/provider.dart';

class SplashScreen extends StatefulWidget {
  String? initialPayload;
  SplashScreen({super.key, this.initialPayload});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    Constants.loadBaseUrl().then((_) async{
      if(context.mounted){
        await Provider.of<AuthController>(context, listen: false).getCurrentUser();
      }
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    print('SplashScreen: build');
    return Consumer<AuthController>(
      builder: (context, authController, _) {
        return getScreen(authController);
      },
    );
  }

  Widget getScreen(AuthController authController) {
    log('Status iix: ${authController.status} ${widget.initialPayload} hhl');
    
    // Only rebuild authenticated screen when currentPage changes
    if(authController.status == AuthStatus.authenticated){
      return _buildAuthenticatedScreen();
    }
    
    if(authController.status == AuthStatus.unauthenticated){
      return const SignInWithGoogleScreen();
    }
    
    return _buildLoadingScreen();
  }

  // Separate widget to isolate color changes
  Widget _buildAuthenticatedScreen() {
    return AnimatedContainer(
      duration: const Duration(milliseconds: 150), // Smooth color transition
      color: Color(0xff00ffA),
      child: const SafeArea(child: MainScreen()),
    );
  }

  Widget _buildLoadingScreen() {
    return Scaffold(
      backgroundColor: Colors.transparent,
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [
              Color(0xFFFF9C00),
              Color(0xFFFD9F10),
              Color(0xFFFCA41E),
              Color(0xFFFCAC32),
              Color(0xFFFDB342),
            ],
          ),
        ),
        child: Stack(
          children: [
            Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Image.asset(
                    'assets/logo.png',
                    width: 140,
                    height: 140,
                  ),
                  const SizedBox(height: 32),
                  Column(
                    children: [
                      Text(
                        'Job Hunter',
                        style: GoogleFonts.montserrat(
                          fontSize: 34,
                          fontWeight: FontWeight.w300,
                          color: Colors.white,
                          letterSpacing: 2.0,
                          height: 1.1,
                        ),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        'by JS Enterprise',
                        style: TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.w400,
                          color: Colors.white.withOpacity(0.8),
                          letterSpacing: 1.2,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 40),
                  CircularProgressIndicator(
                    valueColor: const AlwaysStoppedAnimation<Color>(Colors.white),
                    backgroundColor: Colors.white.withOpacity(0.1),
                    strokeWidth: 3.0,
                  ),
                ],
              ),
            ),
            Align(
              alignment: Alignment.bottomCenter,
              child: Padding(
                padding: const EdgeInsets.only(bottom: 40.0),
                child: Image.asset(
                  'assets/js_enterprise.png',
                  width: 100,
                  height: 100,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}