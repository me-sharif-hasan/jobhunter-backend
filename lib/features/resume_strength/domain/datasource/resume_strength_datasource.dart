import 'dart:developer';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/domain/model/api_response.dart';
import 'package:personalized_job_hunter/features/resume_strength/domain/model/resume_strength_model.dart';
import 'package:personalized_job_hunter/util/http/client.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

class ResumeStrengthDataSource {

  GetIt locator = GetIt.instance;
  late BackendClient _client;

  ResumeStrengthDataSource() {
    _client = locator<BackendClient>();
  }
  
  Future<ResumeStrengthModel> getResumeStrength(String jobId) async {
    try {
      log('Fetching resume strength for job ID: $jobId');
      
      final response = await _client.get('${Constants.getResumeStrength}?job_id=$jobId');

      if (response.statusCode == 200) {
        final ApiResponse apiResponse = ApiResponse.fromResponseUtf8(response);
        
        if (apiResponse.success && apiResponse.data != null) {
          log('Resume strength fetched successfully: ${apiResponse.data['score']}');
          return ResumeStrengthModel.fromJson(apiResponse.data);
        } else {
          final errorMessage = apiResponse.message;
          log('API returned error: $errorMessage');
          throw Exception('Failed to get resume strength: $errorMessage');
        }
      } else {
        log('HTTP error: ${response.statusCode}');
        throw Exception('Failed to get resume strength: HTTP ${response.statusCode}');
      }
    } catch (e) {
      log('Error getting resume strength: $e');
      throw Exception('Error getting resume strength: $e');
    }
  }
}
