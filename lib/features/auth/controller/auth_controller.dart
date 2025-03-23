import 'dart:developer';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/auth/domain/models/user_data_model.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../../main.dart';
import '../domain/datasource/auth_datasource.dart';
import '../domain/models/user_registration_model.dart';

class AuthController extends ChangeNotifier {
  GetIt locator = GetIt.instance;

  AuthDatasource get authDatasource => locator<AuthDatasource>();
  UserDataModel? userDataModel;

  AuthStatus _status = AuthStatus.uninitialized;
  bool isLoading = false;
  get status => _status;

  void getCurrentUser() async {
    _status = AuthStatus.uninitialized;
    try {
      userDataModel = await authDatasource.getCurrentUser();
      _status = AuthStatus.authenticated;
      notifyListeners();
    } catch (e) {
      _status = AuthStatus.unauthenticated;
      notifyListeners();
    }
  }

  void storeTokenInSharedPref(String token) async {
    try{
      SharedPreferences prefs = await SharedPreferences.getInstance();
      await prefs.setString('token', token);
    }catch(e){
      log("msg $e");
    }
  }

  Future loginWithGoogle(UserRegistrationModel userRegistrationModel) async {
    try {
      isLoading = true;
      log("$userRegistrationModel hello world");
      _status = AuthStatus.uninitialized;
      log("Status: $_status: ${userRegistrationModel.toJson()}");
      notifyListeners();
      String token = await authDatasource.registration(userRegistrationModel);
      log("Toke is: "+token);
      storeTokenInSharedPref(token);
      _status = AuthStatus.authenticated;
    } catch (e) {
      log("$e");
      _status = AuthStatus.unauthenticated;
    } finally {
      isLoading = false;
      notifyListeners();
    }
  }

  void logout() {
    storeTokenInSharedPref("");
    Provider.of<MetaController>(navigatorKey.currentContext!, listen: false).currentPage=1;
    _status = AuthStatus.unauthenticated;
    notifyListeners();
  }
}

enum AuthStatus { uninitialized, authenticated, unauthenticated }
