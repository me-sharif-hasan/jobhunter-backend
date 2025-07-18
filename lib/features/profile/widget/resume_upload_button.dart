import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:file_picker/file_picker.dart';
import 'package:provider/provider.dart';
import 'package:personalized_job_hunter/features/profile/controller/resume_upload_controller.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

class ResumeUploadButton extends StatefulWidget {
  const ResumeUploadButton({super.key});

  @override
  State<ResumeUploadButton> createState() => _ResumeUploadButtonState();
}

class _ResumeUploadButtonState extends State<ResumeUploadButton> {
  bool _isPressed = false;

  @override
  Widget build(BuildContext context) {
    return Consumer<ResumeUploadController>(
      builder: (context, controller, _) {
        return Column(
          children: [
            AnimatedScale(
              scale: _isPressed ? 0.96 : 1.0,
              duration: const Duration(milliseconds: 100),
              child: Container(
                width: double.infinity,
                margin: const EdgeInsets.symmetric(horizontal: 24),
                decoration: BoxDecoration(
                  color: Constants.getThemeColor(1)[0], // Solid orange color
                  borderRadius: BorderRadius.circular(16),
                  boxShadow: [
                    BoxShadow(
                      color: Constants.getThemeColor(1)[0].withOpacity(0.2),
                      blurRadius: 6,
                      offset: const Offset(0, 3),
                    ),
                  ],
                ),
                child: Material(
                  color: Colors.transparent,
                  borderRadius: BorderRadius.circular(16),
                  child: InkWell(
                    borderRadius: BorderRadius.circular(16),
                    onTap: controller.isUploading ? null : () => _pickAndUploadFile(context, controller),
                    onTapDown: (_) => setState(() => _isPressed = true),
                    onTapUp: (_) => setState(() => _isPressed = false),
                    onTapCancel: () => setState(() => _isPressed = false),
                    child: Container(
                      padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 24),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          controller.isUploading
                              ? const SizedBox(
                                  width: 20,
                                  height: 20,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2.5,
                                    valueColor: AlwaysStoppedAnimation<Color>(Colors.white),
                                  ),
                                )
                              : const Icon(Icons.upload_file_rounded, size: 24, color: Colors.white),
                          const SizedBox(width: 12),
                          Text(
                            controller.isUploading ? 'Uploading...' : 'Upload Resume (PDF)',
                            style: const TextStyle(
                              fontSize: 16,
                              fontWeight: FontWeight.w600,
                              color: Colors.white,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ),
            if (controller.errorMessage != null) ...[
              const SizedBox(height: 16),
              _buildMaterialMessage(
                message: controller.errorMessage!,
                icon: Icons.error_rounded,
                isSuccess: false,
                onDismiss: controller.clearMessages,
              ),
            ],
            if (controller.successMessage != null) ...[
              const SizedBox(height: 16),
              _buildMaterialMessage(
                message: controller.successMessage!,
                icon: Icons.check_circle_rounded,
                isSuccess: true,
                onDismiss: controller.clearMessages,
              ),
            ],
          ],
        );
      },
    );
  }

  Future<void> _pickAndUploadFile(BuildContext context, ResumeUploadController controller) async {
    try {
      FilePickerResult? result;

      try {
        result = await FilePicker.platform.pickFiles(
          type: FileType.custom,
          allowedExtensions: ['pdf'],
          allowMultiple: false,
        );
      } on MissingPluginException {
        if (context.mounted) {
          _showPluginMissingDialog(context);
        }
        return;
      } on PlatformException catch (e) {
        if (context.mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Platform error: ${e.message}'),
              backgroundColor: Colors.red,
            ),
          );
        }
        return;
      }

      if (result != null && result.files.isNotEmpty) {
        if (kIsWeb) {
          final bytes = result.files.first.bytes;
          final fileName = result.files.first.name;
          if (bytes != null) {
            await controller.uploadResumeBytes(bytes, fileName);
          } else {
            throw Exception('No file bytes available on Web');
          }
        } else {
          final filePath = result.files.first.path;
          if (filePath != null) {
            final file = File(filePath);
            await controller.uploadResume(file);
          } else {
            throw Exception('File path is null on Android');
          }
        }
      } else {
        if (context.mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('No file selected'),
              backgroundColor: Colors.grey,
            ),
          );
        }
      }
    } catch (e) {
      if (context.mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Error uploading file: $e'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  void _showPluginMissingDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('File Picker Not Available'),
        content: const Text(
          'File picker is not properly configured for this platform. '
          'This is a development issue that needs to be resolved.\n\n'
          'For Web: Ensure "universal_html" is imported and try again.\n'
          'For testing: Run "flutter clean" and rebuild\n'
          'Ensure you\'re on a supported platform (Android/Web)',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('OK'),
          ),
        ],
      ),
    );
  }

  Widget _buildMaterialMessage({
    required String message,
    required IconData icon,
    required bool isSuccess,
    required VoidCallback onDismiss,
  }) {
    final primaryColor = isSuccess 
        ? Constants.getThemeColor(3)[0] // Use app's theme color for success (purple/indigo)
        : Constants.getThemeColor(5)[0]; // Use app's theme color for error (pink)

    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 20),
      child: Material(
        elevation: 4,
        borderRadius: BorderRadius.circular(12),
        shadowColor: Colors.black.withOpacity(0.1),
        child: Container(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(12),
            color: Colors.white,
            border: Border.all(
              color: primaryColor.withOpacity(0.2),
              width: 1,
            ),
          ),
          child: Row(
            children: [
              Container(
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(
                  color: primaryColor.withOpacity(0.1),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Icon(
                  icon,
                  color: primaryColor,
                  size: 20,
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text(
                      isSuccess ? 'Success!' : 'Upload Failed',
                      style: TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.w600,
                        color: primaryColor,
                      ),
                    ),
                    const SizedBox(height: 2),
                    Text(
                      message,
                      style: TextStyle(
                        fontSize: 13,
                        color: Colors.grey[600],
                        height: 1.2,
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(width: 8),
              Material(
                color: Colors.transparent,
                borderRadius: BorderRadius.circular(16),
                child: InkWell(
                  borderRadius: BorderRadius.circular(16),
                  onTap: onDismiss,
                  child: Container(
                    padding: const EdgeInsets.all(6),
                    child: Icon(
                      Icons.close_rounded,
                      color: Colors.grey[500],
                      size: 18,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}