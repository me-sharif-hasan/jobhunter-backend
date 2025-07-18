import 'dart:developer';
import 'dart:io';
import 'dart:typed_data';
import 'dart:async';
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
  Completer<void>? _uploadCompleter;

  bool get isUploading => _isUploading;
  String? get errorMessage => _errorMessage;
  String? get successMessage => _successMessage;

  Future<void> uploadResume(File file) async {
    if (_isUploading) return; // Prevent multiple uploads
    
    _uploadCompleter = Completer<void>();
    
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
      
      if (_uploadCompleter?.isCompleted == false) {
        _successMessage = 'Resume uploaded successfully!';
        
        // Add haptic feedback for success
        HapticFeedback.mediumImpact();
        
        log('Resume uploaded successfully');
        
        // Auto-dismiss success message after 4 seconds
        Future.delayed(const Duration(seconds: 4), () {
          if (_successMessage != null && !(_uploadCompleter?.isCompleted ?? true)) {
            _clearMessages();
          }
        });
        
        _uploadCompleter?.complete();
      }
      
    } catch (e) {
      if (_uploadCompleter?.isCompleted == false) {
        log('Error uploading resume: $e');
        _errorMessage = e.toString().replaceAll('Exception: ', '');
        
        // Add haptic feedback for error
        HapticFeedback.lightImpact();
        
        // Auto-dismiss error message after 6 seconds
        Future.delayed(const Duration(seconds: 6), () {
          if (_errorMessage != null && !(_uploadCompleter?.isCompleted ?? true)) {
            _clearMessages();
          }
        });
        
        _uploadCompleter?.completeError(e);
      }
    } finally {
      _setUploading(false);
      _uploadCompleter = null;
    }
  }

  Future<void> uploadResumeBytes(Uint8List bytes, String fileName) async {
    if (_isUploading) return; // Prevent multiple uploads
    
    _uploadCompleter = Completer<void>();
    
    try {
      _setUploading(true);
      _clearMessages();
      
      // Check file size (1MB = 1048576 bytes)
      if (bytes.length > 1048576) {
        throw Exception('File size exceeds 1MB limit');
      }

      // Check file extension
      if (!fileName.toLowerCase().endsWith('.pdf')) {
        throw Exception('Only PDF files are allowed');
      }
      
      log('Uploading resume bytes: $fileName');
      
      await _dataSource.uploadResumeBytes(bytes, fileName);
      
      if (_uploadCompleter?.isCompleted == false) {
        _successMessage = 'Resume uploaded successfully!';
        
        // Add haptic feedback for success
        HapticFeedback.mediumImpact();
        
        log('Resume uploaded successfully');
        
        // Auto-dismiss success message after 4 seconds
        Future.delayed(const Duration(seconds: 4), () {
          if (_successMessage != null && !(_uploadCompleter?.isCompleted ?? true)) {
            _clearMessages();
          }
        });
        
        _uploadCompleter?.complete();
      }
      
    } catch (e) {
      if (_uploadCompleter?.isCompleted == false) {
        log('Error uploading resume: $e');
        _errorMessage = e.toString().replaceAll('Exception: ', '');
        
        // Add haptic feedback for error
        HapticFeedback.lightImpact();
        
        // Auto-dismiss error message after 6 seconds
        Future.delayed(const Duration(seconds: 6), () {
          if (_errorMessage != null && !(_uploadCompleter?.isCompleted ?? true)) {
            _clearMessages();
          }
        });
        
        _uploadCompleter?.completeError(e);
      }
    } finally {
      _setUploading(false);
      _uploadCompleter = null;
    }
  }

  void _setUploading(bool uploading) {
    _isUploading = uploading;
    notifyListeners();
  }

  void _clearMessages() {
    _errorMessage = null;
    _successMessage = null;
    notifyListeners();
  }

  void clearMessages() {
    _clearMessages();
  }

  void cancelUpload() {
    if (_isUploading && _uploadCompleter != null && !_uploadCompleter!.isCompleted) {
      log('Cancelling upload...');
      _uploadCompleter!.completeError(Exception('Upload cancelled by user'));
      _errorMessage = 'Upload cancelled';
      _setUploading(false);
      _uploadCompleter = null;
      
      // Add haptic feedback for cancellation
      HapticFeedback.lightImpact();
    }
  }
}
