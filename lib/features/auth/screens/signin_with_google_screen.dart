import 'package:animate_do/animate_do.dart'; // For animations
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:personalized_job_hunter/features/auth/controller/auth_controller.dart';
import 'package:personalized_job_hunter/features/auth/domain/models/user_registration_model.dart';
import 'package:provider/provider.dart';

const List<String> scopes = <String>[
  'email',
];

GoogleSignIn _googleSignIn = GoogleSignIn(
  scopes: scopes,
);

class SignInWithGoogleScreen extends StatefulWidget {
  const SignInWithGoogleScreen({super.key});

  @override
  State<SignInWithGoogleScreen> createState() => _SignInWithGoogleScreenState();
}

class _SignInWithGoogleScreenState extends State<SignInWithGoogleScreen> {
  @override
  Widget build(BuildContext context) {
    return Consumer<AuthController>(
      builder: (context, controller, _) {
        return Scaffold(
          body: Container(
            decoration: const BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
                colors: [
                  Color(0xFFFFA726), // Vibrant Orange
                  Color(0xFFAB47BC), // Bold Purple
                ],
                stops: [0.2, 0.8], // Smooth yet striking transition
              ),
            ),
            child: SafeArea(
              child: SizedBox(
                width: MediaQuery.of(context).size.width,
                height: MediaQuery.of(context).size.height,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    // Top Section: Logo and Title
                    FadeInDown(
                      duration: const Duration(milliseconds: 700),
                      child: Column(
                        children: [
                          // Clean, Flat Logo
                          Container(
                              padding: const EdgeInsets.all(18),
                              decoration: const BoxDecoration(
                                shape: BoxShape.circle,
                              ),
                              child: Image.asset(
                                'assets/logo.png',
                                height: 100,
                              )),
                          const SizedBox(height: 24),
                          // Bold, Flat Title
                          const Text(
                            'Job Hunter',
                            style: TextStyle(
                              fontSize: 40,
                              fontWeight: FontWeight.w800,
                              color: Colors.white,
                              letterSpacing: 1.2,
                            ),
                          ),
                          const SizedBox(height: 10),
                          Padding(
                            padding: const EdgeInsets.all(15.0),
                            child: Text(
                              'Join Job Hunter to find your dream job from your personalized job timeline of selected companies.',
                              style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.w400,
                                color: Colors.white.withOpacity(0.85),
                              ),
                              textAlign: TextAlign.center,
                            ),
                          ),
                        ],
                      ),
                    ),
                    const Spacer(flex: 2),
                    FadeInUp(
                      duration: const Duration(milliseconds: 700),
                      child: Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 40.0),
                        child: GestureDetector(
                          onTap: () {
                            _googleSignIn.signIn().then((value) {
                              if (value == null) return;
                              String? email = value.email;
                              String? name = value.displayName;
                              String? photoUrl = value.photoUrl;
                              String? serverAuthCode = value.serverAuthCode;
                              if (name != null) {
                                UserRegistrationModel userRegistrationModel =
                                    UserRegistrationModel(
                                  email: email,
                                  name: name,
                                  imageUrl: "$photoUrl",
                                  token: "$serverAuthCode",
                                );
                                try{
                                  controller
                                      .loginWithGoogle(userRegistrationModel);
                                }catch(e){
                                  print(e);
                                }
                              }
                            });
                          },
                          child: Container(
                            padding: const EdgeInsets.symmetric(
                                vertical: 16, horizontal: 28),
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.circular(8),
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.black.withOpacity(0.1),
                                  blurRadius: 8,
                                  offset: const Offset(0, 4),
                                ),
                              ],
                            ),
                            child: Row(
                              mainAxisSize: MainAxisSize.min,
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                !controller.isLoading?Image.asset(
                                  'assets/google_logo.png',
                                  height: 22,
                                ):const CircularProgressIndicator(),
                                const SizedBox(width: 12),
                                const Text(
                                  'Continue  with Google',
                                  style: TextStyle(
                                    fontSize: 18,
                                    color: Color(0xFF1F1F1F), // Purple accent
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ),
                    ),
                    const Spacer(flex: 3),
                  ],
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}
