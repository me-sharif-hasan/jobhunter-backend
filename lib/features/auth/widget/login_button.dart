// login_button.dart
import 'package:flutter/material.dart';
import '../controller/user_registration_controller.dart';

class LoginButton extends StatelessWidget {
  final GlobalKey<FormState> formKey;
  final UserRegistrationController? controllerProvider;
  final String? buttonText; // Optional button text
  final VoidCallback? onPressed; // Optional custom onPressed

  const LoginButton({
    super.key,
    required this.formKey,
    this.controllerProvider,
    this.buttonText,
    this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      child: ElevatedButton(
        onPressed: onPressed,
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
          backgroundColor: const Color(0xFFFFA726), // Vibrant orange
          elevation: 5,
        ),
        child: Text(
          buttonText ?? 'Login',
          style: const TextStyle(
            fontSize: 18,
            fontWeight: FontWeight.bold,
            color: Colors.white,
          ),
        ),
      ),
    );
  }
}