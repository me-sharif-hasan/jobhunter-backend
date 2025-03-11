import 'dart:developer';

import 'package:flutter/cupertino.dart';
import 'package:flutter_login_facebook/flutter_login_facebook.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/profile/datasource/profile_datasource.dart';
import 'package:personalized_job_hunter/util/http/client.dart';

class FacebookController extends ChangeNotifier {

  GetIt locator = GetIt.instance;
  late ProfileDatasource _profileDatasource;
  final String configId='604163769184759';

  FacebookController(){
    _profileDatasource = locator<ProfileDatasource>();
  }
  final fb = FacebookLogin();
  Future connectWithFacebook() async{
    final res = await fb.logIn(permissions: [
      FacebookPermission.publicProfile,
      FacebookPermission.email,
      FacebookPermission.pagesReadEngagement,
      FacebookPermission.userManagedGroups
    ]);
    if(res.status==FacebookLoginStatus.success){
      String? accessToken = res.accessToken?.token;
      if(accessToken!=null){
        log("Access Token: "+accessToken);
        await _profileDatasource.connectFacebook(accessToken);
      }
    }else{
      throw Exception("Facebook login failed");
    }
  }
}