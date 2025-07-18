import 'dart:developer';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/resume_strength/domain/datasource/resume_strength_datasource.dart';
import 'package:personalized_job_hunter/features/resume_strength/domain/model/resume_strength_model.dart';

class ResumeStrengthController extends ChangeNotifier {
  final ResumeStrengthDataSource _dataSource = GetIt.instance<ResumeStrengthDataSource>();

  ResumeStrengthController();

  ResumeStrengthModel? _resumeStrength;
  bool _isLoading = false;
  String? _errorMessage;

  ResumeStrengthModel? get resumeStrength => _resumeStrength;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  Future<void> getResumeStrength(String jobId) async {
    try {
      _setLoading(true);
      _clearError();
      
      log('Getting resume strength for job: $jobId');
      
      final result = await _dataSource.getResumeStrength(jobId);
      _resumeStrength = result;
      
      log('Resume strength retrieved successfully: Score ${result.score}');
      
    } catch (e) {
      log('Error getting resume strength: $e');
      _errorMessage = e.toString();
      _resumeStrength = null;
    } finally {
      _setLoading(false);
    }
  }

  void _setLoading(bool loading) {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _isLoading = loading;
      notifyListeners();
    });
  }

  void _clearError() {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _errorMessage = null;
      notifyListeners();
    });
  }

  void clearData() {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _resumeStrength = null;
      _errorMessage = null;
      _isLoading = false;
      notifyListeners();
    });
  }
}
