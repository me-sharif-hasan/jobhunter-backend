import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../controller/user_registration_controller.dart';
import '../widget/gradient_background.dart';
import '../widget/welcome_header.dart';
import '../widget/custom_text_field.dart';
import '../widget/login_button.dart';
import '../widget/signup_prompt.dart';
import 'login_screen.dart';

class UserSignupScreen extends StatefulWidget {
  const UserSignupScreen({super.key});

  @override
  State<UserSignupScreen> createState() => _UserSignupScreenState();
}

class _UserSignupScreenState extends State<UserSignupScreen> {
  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: GradientBackground(
        child: SafeArea(
          child: Center(
            child: SingleChildScrollView(
              padding: const EdgeInsets.symmetric(horizontal: 32.0, vertical: 16.0),
              child: Consumer<UserRegistrationController>(
                builder: (context, userRegistrationProvider, child) {
                  return Form(
                    key: _formKey,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const WelcomeHeader(
                          title: 'Join the Journey',
                          subtitle: 'Start hunting for your dream job!',
                        ),
                        const SizedBox(height: 40),
                        // Reusing CustomTextField for Name
                        NameField(
                          onChanged: userRegistrationProvider.updateName,
                        ),
                        const SizedBox(height: 16),
                        // Reusing EmailField from login
                        EmailField(
                          onChanged: userRegistrationProvider.updateEmail,
                        ),
                        const SizedBox(height: 16),
                        // Reusing PasswordField from login
                        PasswordField(
                          onChanged: userRegistrationProvider.updatePassword,
                        ),
                        const SizedBox(height: 16),
                        // Confirm Password using CustomTextField
                        CustomTextField(
                          label: 'Confirm Password',
                          hint: 'Re-enter your password',
                          onChanged: (value) {
                            setState(() {
                            });
                          },
                          isPassword: true,
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Please confirm your password';
                            }
                            if (value != userRegistrationProvider.password) {
                              return 'Passwords do not match';
                            }
                            return null;
                          },
                        ),
                        const SizedBox(height: 32),
                        // Reusing LoginButton but adapting for signup
                        LoginButton(
                          formKey: _formKey,
                          controllerProvider: userRegistrationProvider,
                          buttonText: 'Sign Up', // Override the button text
                          onPressed: () {
                            if (_formKey.currentState!.validate()) {
                              // final userRegistrationModel = UserRegistrationModel(
                              //   email: userRegistrationProvider.email,
                              //   password: userRegistrationProvider.password,
                              //   confirmPassword: _confirmPassword??"",
                              //   name: userRegistrationProvider.name,
                              // );
                            }
                          },
                        ),
                        const SizedBox(height: 16),
                        SignupPrompt(
                          promptText: 'Already have an account? ',
                          linkText: 'Login',
                          onTap: () {
                            Navigator.of(context).pushReplacement(
                              MaterialPageRoute(
                                builder: (context) => const UserLoginScreen(),
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