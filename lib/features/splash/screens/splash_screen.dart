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
    return Consumer2<AuthController,MetaController >(
      builder: (context, authController,MetaController metaController, _) {
        return getScreen(authController,metaController);
      },
    );
  }

  Widget getScreen(AuthController controller,MetaController metaController) {
    log('Status iix: ${controller.status} ${widget.initialPayload} hhl');
    if(controller.status == AuthStatus.authenticated){
      return Container(
        color: Color(Constants.themeColor[metaController.currentPage][0]),
        child: SafeArea(child: const MainScreen()),
      );
    }
    if(controller.status == AuthStatus.unauthenticated){
      return const SignInWithGoogleScreen();
    }
    return Scaffold(
        backgroundColor: Colors.transparent,
        body: Container(
          decoration: const BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topLeft, // Angular start
              end: Alignment.bottomRight, // Angular finish
              colors: [
                Color(0xFFFF9C00), // Soft orange for warmth
                Color(0xFFFD9F10), // Calming purple for peace
                Color(0xFFFCA41E), // Calming purple for peace
                Color(0xFFFCAC32), // Calming purple for peace
                Color(0xFFFDB342), // Calming purple for peace
              ],
            ),
          ),
          child: Stack(
            children: [
              // Main content
              Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    // Logo
                    Image.asset(
                      'assets/logo.png',
                      width: 140,
                      height: 140,
                    ),
                    const SizedBox(height: 32),
                    // "Job Hunter by JS Enterprise"
                    Column(
                      children: [
                        Text(
                          'Job Hunter',
                          style: GoogleFonts.montserrat(
                            fontSize: 34,
                            fontWeight: FontWeight.w300, // Ultra-light
                            color: Colors.white,
                            letterSpacing: 2.0, // Wide spacing
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
                    // Circular Progress Indicator
                    CircularProgressIndicator(
                      valueColor: const AlwaysStoppedAnimation<Color>(Colors.white),
                      backgroundColor: Colors.white.withOpacity(0.1),
                      strokeWidth: 3.0,
                    ),
                  ],
                ),
              ),
              // JS Enterprise logo at bottom
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