// signup_prompt.dart
import 'package:flutter/material.dart';

class SignupPrompt extends StatelessWidget {
  final String? promptText;
  final String? linkText;
  final VoidCallback? onTap;

  const SignupPrompt({
    super.key,
    this.promptText,
    this.linkText,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Text(
          promptText ?? 'Don’t have an account? ',
          style: TextStyle(color: Colors.grey.shade600),
        ),
        GestureDetector(
          onTap: onTap ?? () {
            // Default navigation (implement navigation here)
          },
          child: Text(
            linkText ?? 'Sign Up',
            style: const TextStyle(
              color: Color(0xFFFFA726),
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ],
    );
  }
}