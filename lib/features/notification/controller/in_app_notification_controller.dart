import 'dart:developer';

import 'package:flutter/cupertino.dart';
import 'package:get_it/get_it.dart';

import '../domain/datasource/notification_datasource.dart';
import '../domain/model/in_app_notification_model.dart';

class InAppNotificationController extends ChangeNotifier{
  GetIt locator = GetIt.instance;
  NotificationDatasource? notificationDatasource;
  InAppNotificationController() {
    notificationDatasource = locator<NotificationDatasource>();
  }
  List<InAppNotification> notifications = [];

  bool isLoading = false;


  Future loadNotifications({int page = 0, int limit = 20}) async {
    try{
      isLoading = true;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        notifyListeners();
      });
      List<InAppNotification> loadedNotifications = await notificationDatasource!.getNotifications(limit,page);
      log("Loaded notifications: $loadedNotifications");
      notifications = loadedNotifications;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        notifyListeners();
      });
    }catch(e){
      log("error loading notification: $e");
    }finally{
      isLoading = false;
      notifyListeners();
    }
  }
}