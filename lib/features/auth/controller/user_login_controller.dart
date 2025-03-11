import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/auth/domain/models/user_data_model.dart';
import 'package:personalized_job_hunter/features/job/screens/job_timelime_screen.dart';

import '../domain/datasource/auth_datasource.dart';

class UserLoginController extends ChangeNotifier {
  String _email = '';
  String _password = '';
  String _confirmPassword = '';
  String _name = '';

  GetIt locator = GetIt.instance;


  String get email => _email;
  String get password => _password;
  String get confirmPassword => _confirmPassword;
  String get name => _name;
  AuthDatasource get authDatasource => locator<AuthDatasource>();

  UserDataModel? userDataModel;

  void getCurrentUser() async{
    userDataModel = await authDatasource.getCurrentUser();
    notifyListeners();
  }

  void updateName(String name) {
    _name = name;
    notifyListeners();
  }

  void updateEmail(String email) {
    _email = email;
    notifyListeners();
  }

  void updatePassword(String password) {
    _password = password;
    notifyListeners();
  }

  void updateConfirmPassword(String confirmPassword) {
    _confirmPassword = confirmPassword;
    notifyListeners();
  }

  void login(BuildContext context) {
    print('Email: $_email, Password: $_password');
    Navigator.of(context).push(
        (MaterialPageRoute(builder: (context) => const JobTimelineScreen())
    ));
  }
}