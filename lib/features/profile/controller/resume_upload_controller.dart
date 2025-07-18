import 'dart:developer';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/profile/datasource/resume_upload_datasource.dart';

class ResumeUploadController extends ChangeNotifier {
  final ResumeUploadDataSource _dataSource = GetIt.instance<ResumeUploadDataSource>();

  ResumeUploadController();

  bool _isUploading = false;
  String? _errorMessage;
  String? _successMessage;

  bool get isUploading => _isUploading;
  String? get errorMessage => _errorMessage;
  String? get successMessage => _successMessage;

  Future<void> uploadResume(File file) async {
    try {
      _setUploading(true);
      _clearMessages();
      
      // Check file size (1MB = 1048576 bytes)
      final fileSize = await file.length();
      if (fileSize > 1048576) {
        throw Exception('File size exceeds 1MB limit');
      }

      // Check file extension
      final fileName = file.path.toLowerCase();
      if (!fileName.endsWith('.pdf')) {
        throw Exception('Only PDF files are allowed');
      }
      
      log('Uploading resume: ${file.path}');
      
      await _dataSource.uploadResume(file.path);
      _successMessage = 'Resume uploaded successfully!';
      
      // Add haptic feedback for success
      HapticFeedback.mediumImpact();
      
      log('Resume uploaded successfully');
      
      // Auto-dismiss success message after 4 seconds
      Future.delayed(const Duration(seconds: 4), () {
        if (_successMessage != null) {
          _clearMessages();
        }
      });
      
    } catch (e) {
      log('Error uploading resume: $e');
      _errorMessage = e.toString();
      _successMessage = null;
    } finally {
      _setUploading(false);
    }
  }

  void _setUploading(bool uploading) {
    if (WidgetsBinding.instance.lifecycleState != null) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _isUploading = uploading;
        notifyListeners();
      });
    }
  }

  void _clearMessages() {
    if (WidgetsBinding.instance.lifecycleState != null) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _errorMessage = null;
        _successMessage = null;
        notifyListeners();
      });
    }
  }

  void clearMessages() {
    _clearMessages();
  }
}
