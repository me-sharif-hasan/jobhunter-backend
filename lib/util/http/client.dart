import 'dart:convert';
import 'dart:developer';
import 'dart:typed_data';

import 'package:flutter_timezone/flutter_timezone.dart';
import 'package:http/http.dart' as http;
import 'package:personalized_job_hunter/features/auth/controller/auth_controller.dart';
import 'package:personalized_job_hunter/main.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../values/constants.dart';
class BackendClient {
  final String token;

  BackendClient(this.token);

  Future<String> getTimezoneId() async{
    //get system timezone
    // String timezone = DateTime.now().timeZoneName;
    final String currentTimeZone = await FlutterTimezone.getLocalTimezone();
    return currentTimeZone;
  }

  Future<http.Response> get(String path) async {
    log(Uri.parse(Constants.baseUrl + path).toString());
    String token = await _getToken();
    log('Saved token: $token');
    http.Response response = await http.get(
      Uri.parse(Constants.baseUrl + path),
      headers: {
        'Authorization': 'Bearer $token',
        'Time-Zone': await getTimezoneId(),
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
        'Time-Zone': await getTimezoneId(),
      },
      body: jsonEncode(body),
    );
    if(response.statusCode==403){
      _routeToLogin();
    }
    return response;
  }

  Future<http.Response> upload(String path, String filePath, String uploadKey) async {
    log(Uri.parse(Constants.baseUrl + path).toString());
    String token = await _getToken();
    log('Saved token: $token');
    
    var request = http.MultipartRequest('POST', Uri.parse(Constants.baseUrl + path));
    request.headers.addAll({
      'Authorization': 'Bearer $token',
      'Time-Zone': await getTimezoneId(),
    });
    
    request.files.add(await http.MultipartFile.fromPath(uploadKey, filePath));
    
    var streamedResponse = await request.send();
    var response = await http.Response.fromStream(streamedResponse);
    
    if(response.statusCode == 403){
      _routeToLogin();
    }
    return response;
  }

  Future<http.Response> uploadBytes(String path, Uint8List bytes, String fileName, String uploadKey) async {
    log(Uri.parse(Constants.baseUrl + path).toString());
    String token = await _getToken();
    log('Saved token: $token');
    
    var request = http.MultipartRequest('POST', Uri.parse(Constants.baseUrl + path));
    request.headers.addAll({
      'Authorization': 'Bearer $token',
      'Time-Zone': await getTimezoneId(),
    });
    
    request.files.add(http.MultipartFile.fromBytes(uploadKey, bytes, filename: fileName));
    
    var streamedResponse = await request.send();
    var response = await http.Response.fromStream(streamedResponse);
    
    if(response.statusCode == 403){
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