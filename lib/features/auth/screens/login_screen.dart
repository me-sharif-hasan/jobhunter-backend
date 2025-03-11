import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/auth/screens/user_signup_Screen.dart';
import 'package:provider/provider.dart';
import '../controller/user_login_controller.dart';
import '../widget/custom_text_field.dart';
import '../widget/gradient_background.dart';
import '../widget/login_button.dart';
import '../widget/signup_prompt.dart';
import '../widget/welcome_header.dart';
class UserLoginScreen extends StatefulWidget {
  const UserLoginScreen({super.key});

  @override
  State<UserLoginScreen> createState() => _UserLoginScreenState();
}

class _UserLoginScreenState extends State<UserLoginScreen> {
  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: GradientBackground(
        child: SafeArea(
          child: Center(
            child: SingleChildScrollView(
              padding: const EdgeInsets.symmetric(horizontal: 32.0, vertical: 16.0),
              child: Consumer<UserLoginController>(
                builder: (context, userLoginProvider, child) {
                  return Form(
                    key: _formKey,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const WelcomeHeader(),
                        const SizedBox(height: 40),
                        EmailField(
                          onChanged: userLoginProvider.updateEmail,
                        ),
                        const SizedBox(height: 16),
                        PasswordField(
                          onChanged: userLoginProvider.updatePassword,
                        ),
                        const SizedBox(height: 32),
                        LoginButton(
                          formKey: _formKey,
                          onPressed: () {
                            if (_formKey.currentState!.validate()) {
                              _formKey.currentState!.save();
                              userLoginProvider.login(context);
                            }
                          },
                        ),
                        const SizedBox(height: 16),
                        SignupPrompt(
                          promptText: 'Don’t have an account? ',
                          linkText: 'Sign Up',
                          onTap: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const UserSignupScreen(),
                              ),
                            );
                          },
                        ),
                      ],
                    ),
                  );
                },
              ),
            ),
          ),
        ),
      ),
    );
  }
}