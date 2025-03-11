import 'dart:convert';
import 'dart:developer';

import 'package:http/http.dart' as http;
import 'package:personalized_job_hunter/features/auth/controller/auth_controller.dart';
import 'package:personalized_job_hunter/main.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../values/constants.dart';
class BackendClient {
  final String token;

  BackendClient(this.token);

  String getTimezoneId(){
    //get system timezone
    String timezone = DateTime.now().timeZoneName;
    return timezone;
  }

  Future<http.Response> get(String path) async {
    log(Uri.parse(Constants.baseUrl + path).toString());
    String token = await _getToken();
    log('Saved token: $token');
    http.Response response = await http.get(
      Uri.parse(Constants.baseUrl + path),
      headers: {
        'Authorization': 'Bearer $token',
        'Time-Zone': getTimezoneId(),
      },
    );
    if(response.statusCode==403){
      _routeToLogin();
    }
    return response;
  }

  Future<http.Response> post(String path, {required Map<String, dynamic> body}) async {
    log(Uri.parse(Constants.baseUrl + path).toString());
    String token = await _getToken();
    log('Saved token: $token');
    http.Response response = await http.post(
      Uri.parse(Constants.baseUrl+ path),
      headers: {
        'Authorization': 'Bearer $token',
        'Content-Type': 'application/json',
        'Time-Zone': getTimezoneId(),
      },
      body: jsonEncode(body),
    );
    if(response.statusCode==403){
      _routeToLogin();
    }
    return response;
  }

  _getToken() async {
    return await SharedPreferences.getInstance().then((value) => value.getString('token') ?? '');
  }

  _routeToLogin(){
    Provider.of<AuthController>(navigatorKey.currentContext!, listen: false).logout();
  }
}