import 'package:flutter/cupertino.dart';

class UserRegistrationController extends ChangeNotifier {
  String _email = '';
  String _password = '';
  String _confirmPassword = '';
  String _name = '';

  String get email => _email;
  String get password => _password;
  String get confirmPassword => _confirmPassword;
  String get name => _name;


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

  void login() {
    print('Email: $_email, Password: $_password');
  }
}