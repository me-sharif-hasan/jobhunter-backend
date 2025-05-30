import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/domain/model/api_response.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

import '../../../../util/http/client.dart';

class BackendMetaDatasource{
  GetIt locator = GetIt.instance;
  BackendClient? client;
  BackendMetaDatasource() {
    client = locator<BackendClient>();
  }

  Future savePushNotificationToken(String token) {
    return client!.post(Constants.saveFirebasePushToken, body: {"fcmToken": token}).then((response) {
      if (response.statusCode == HttpStatus.ok) {
        log("--->${response.body}");
        final dynamic data = jsonDecode(utf8.decode(response.bodyBytes));
        ApiResponse apiResponse = ApiResponse.fromJson(data);
        log(data.toString());
        return apiResponse;
      } else {
        throw Exception('Failed to save push notification token');
      }
    });
  }

  Future<ApiResponse> getJobAppliedOptions() async{
    return client!.get(Constants.getJobAppliedOptions).then((response) {
      if (response.statusCode == HttpStatus.ok) {
        final dynamic data = jsonDecode(utf8.decode(response.bodyBytes));
        ApiResponse apiResponse = ApiResponse.fromJson(data);
        log(data.toString());
        return apiResponse;
      } else {
        throw Exception('Failed to fetch job applied options');
      }
    });
  }
}