import 'dart:developer';

import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/widgets.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/domain/datasource/backend_meta_datasource.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

import '../../job/screens/job_timelime_screen.dart';
import '../../notification/screens/in_app_notification_screen.dart';
import '../../profile/screens/user_profile_screen.dart';
import '../../subscriptions/screens/company_subscription_screen.dart';

class MetaController extends ChangeNotifier {
  int _currentPage = 1;
  bool _loadingData = false;

  bool get loadingData => _loadingData;
  static Map<String, dynamic> notificationPayload = {};
  GetIt locator = GetIt.instance;
  BackendMetaDatasource? _backendMetaDatasource;

  MetaController() {
    _backendMetaDatasource = locator<BackendMetaDatasource>();
  }

  static BuildContext? mainPageBuildContext;

  set loadingData(bool value) {
    _loadingData = value;
    notifyListeners();
  }

  String _currentScreenTitle = 'Job Timeline';

  set currentPage(int value) {
    _currentPage = value;
    switch (value) {
      case 0:
        _currentScreenTitle = 'Profile';
        break;
      case 1:
        _currentScreenTitle = 'Job Timeline';
        break;
      case 2:
        _currentScreenTitle = 'Company Subscriptions';
        break;
      case 3:
        _currentScreenTitle = 'Notifications';
        break;
      case 4:
        _currentScreenTitle = 'Settings';
        break;
    }
    notifyListeners();
  }

  int get currentPage => _currentPage;

  final List<Widget> _screens = <Widget>[
    const UserProfileScreen(),
    const JobTimelineScreen(),
    const CompanySubscriptionScreen(),
    const InAppNotificationScreen(),
  ];

  get currentScreenTitle => _currentScreenTitle;

  get screens => _screens;

  Widget getScreen(int index) {
    return _screens[index];
  }

  List<Color> getThemeColor() {
    List<Color> colors = [
      Color(Constants.themeColor[currentPage][0]),
      Color(Constants.themeColor[currentPage][1]),
    ];
    return colors;
  }

  getFcmPushToken() async {
    try {
      String? fcmToken = await FirebaseMessaging.instance.getToken();
      log('FCM Token: $fcmToken');
      await _backendMetaDatasource!.savePushNotificationToken(fcmToken!);
    } catch (e) {
      log('Error getting FCM token: $e');
    }
  }
}
