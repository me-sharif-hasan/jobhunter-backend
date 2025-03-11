import 'package:flutter/widgets.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

import '../../job/screens/job_timelime_screen.dart';
import '../../profile/screens/user_profile_screen.dart';
import '../../subscriptions/screens/company_subscription_screen.dart';

class MetaController extends ChangeNotifier {
  int _currentPage = 1;
  bool _loadingData = false;
  bool get loadingData => _loadingData;
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
    const Placeholder(),
  ];

  get currentScreenTitle => _currentScreenTitle;

  get screens => _screens;

  Widget getScreen(int index) {
    return _screens[index];
  }

  List <Color> getThemeColor() {
    List <Color> colors = [
      Color(Constants.themeColor[currentPage][0]),
      Color(Constants.themeColor[currentPage][1]),
    ];
    return colors;
  }
}