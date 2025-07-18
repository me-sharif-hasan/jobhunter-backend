import 'dart:developer';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/domain/model/api_response.dart';
import 'package:personalized_job_hunter/util/http/client.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

class ResumeUploadDataSource {
  GetIt locator = GetIt.instance;
  late BackendClient _client;

  ResumeUploadDataSource() {
    _client = locator<BackendClient>();
  }
  
  Future<ApiResponse> uploadResume(String filePath) async {
    try {
      log('Uploading resume file: $filePath');
      
      final response = await _client.upload(Constants.uploadResume, filePath, 'file');

      if (response.statusCode == 200) {
        final ApiResponse apiResponse = ApiResponse.fromResponseUtf8(response);
        
        if (apiResponse.success) {
          log('Resume uploaded successfully');
          return apiResponse;
        } else {
          final errorMessage = apiResponse.message;
          log('API returned error: $errorMessage');
          throw Exception('Failed to upload resume: $errorMessage');
        }
      } else {
        log('HTTP error: ${response.statusCode}');
        throw Exception('Failed to upload resume: HTTP ${response.statusCode}');
      }
    } catch (e) {
      log('Error uploading resume: $e');
      throw Exception('Error uploading resume: $e');
    }
  }
}
