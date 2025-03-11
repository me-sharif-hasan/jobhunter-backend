// custom_text_field.dart (Updated)
import 'package:flutter/material.dart';

class CustomTextField extends StatefulWidget {
  final String label;
  final String hint;
  final Function(String) onChanged;
  final TextInputType? keyboardType;
  final bool isPassword;
  final String? Function(String?)? validator;

  const CustomTextField({
    super.key,
    required this.label,
    required this.hint,
    required this.onChanged,
    this.keyboardType,
    this.isPassword = false,
    this.validator,
  });

  @override
  State<CustomTextField> createState() => _CustomTextFieldState();
}

class _CustomTextFieldState extends State<CustomTextField> {
  bool _isPasswordVisible = false;

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      onChanged: widget.onChanged,
      keyboardType: widget.keyboardType,
      obscureText: widget.isPassword && !_isPasswordVisible,
      validator: widget.validator,
      decoration: InputDecoration(
        labelText: widget.label,
        hintText: widget.hint,
        filled: true,
        fillColor: Colors.white.withOpacity(0.9),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
        contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
        suffixIcon: widget.isPassword
            ? IconButton(
          icon: Icon(
            _isPasswordVisible ? Icons.visibility : Icons.visibility_off,
            color: Colors.grey.shade400,
          ),
          onPressed: () {
            setState(() {
              _isPasswordVisible = !_isPasswordVisible;
            });
          },
        )
            : null,
      ),
    );
  }
}

// NameField Implementation
class NameField extends StatelessWidget {
  final Function(String) onChanged;

  const NameField({super.key, required this.onChanged});

  @override
  Widget build(BuildContext context) {
    return CustomTextField(
      label: 'Name',
      hint: 'Enter your full name',
      onChanged: onChanged,
      keyboardType: TextInputType.name,
      validator: (value) {
        if (value == null || value.isEmpty) {
          return 'Please enter your name';
        }
        if (value.length < 2) {
          return 'Name must be at least 2 characters';
        }
        return null;
      },
    );
  }
}

// EmailField Implementation (Already exists)
class EmailField extends StatelessWidget {
  final Function(String) onChanged;

  const EmailField({super.key, required this.onChanged});

  @override
  Widget build(BuildContext context) {
    return CustomTextField(
      label: 'Email',
      hint: 'Enter your email',
      onChanged: onChanged,
      keyboardType: TextInputType.emailAddress,
      validator: (value) {
        if (value == null || value.isEmpty) {
          return 'Please enter your email';
        }
        if (!RegExp(r'^[^@]+@[^@]+\.[^@]+').hasMatch(value)) {
          return 'Please enter a valid email';
        }
        return null;
      },
    );
  }
}

// PasswordField Implementation (Already exists)
class PasswordField extends StatelessWidget {
  final Function(String) onChanged;

  const PasswordField({super.key, required this.onChanged});

  @override
  Widget build(BuildContext context) {
    return CustomTextField(
      label: 'Password',
      hint: 'Enter your password',
      onChanged: onChanged,
      isPassword: true,
      validator: (value) {
        if (value == null || value.isEmpty) {
          return 'Please enter your password';
        }
        if (value.length < 6) {
          return 'Password must be at least 6 characters';
        }
        return null;
      },
    );
  }
}