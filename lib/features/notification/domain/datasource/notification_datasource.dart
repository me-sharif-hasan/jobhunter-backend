import 'dart:convert';

import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/domain/model/api_response.dart';
import 'package:personalized_job_hunter/features/notification/domain/model/in_app_notification_model.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

import '../../../../util/http/client.dart';

class NotificationDatasource {
  GetIt locator = GetIt.instance;
  BackendClient? client;
  NotificationDatasource() {
    client = locator<BackendClient>();
  }
  Future<List<InAppNotification>> getNotifications(int limit,int page) {
    return client!.get("${Constants.inAppNotification}?limit=$limit&page=$page").then((response) {
      if (response.statusCode == 200) {
        final dynamic data = jsonDecode(utf8.decode(response.bodyBytes));
        ApiResponse apiResponse = ApiResponse.fromJson(data);
        List<dynamic> notifications = apiResponse.data;
        List<InAppNotification> inAppNotifications = [];
        for (var notification in notifications) {
          inAppNotifications.add(InAppNotification.fromJson(notification));
        }
        return inAppNotifications;
      } else {
        throw Exception('Failed to load notifications');
      }
    });
  }
}