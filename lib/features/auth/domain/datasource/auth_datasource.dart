import 'dart:convert';
import 'dart:developer';

import 'package:get_it/get_it.dart';
import 'package:http/http.dart';
import 'package:personalized_job_hunter/features/auth/domain/models/user_data_model.dart';
import 'package:personalized_job_hunter/features/auth/domain/models/user_registration_model.dart';
import 'package:personalized_job_hunter/features/common/domain/model/api_response.dart';
import 'package:personalized_job_hunter/util/http/client.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

class AuthDatasource{
  GetIt locator = GetIt.instance;
  BackendClient get client => locator<BackendClient>();
  Future<UserDataModel> getCurrentUser() async{
    Response response = await client.get(Constants.getCurrentUser);
    ApiResponse apiResponse = ApiResponse.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
    if(apiResponse.success) {
      return UserDataModel.fromJson(apiResponse.data);
    } else {
      throw Exception(apiResponse.message);
    }
  }

  Future<String> registration(UserRegistrationModel registrationModel) async{
    print('Registering...');
    Response response = await client.post(Constants.register, body: registrationModel.toJson());
    log('Response: ${response.body}');
    ApiResponse apiResponse=ApiResponse.fromResponseUtf8(response);
    if(response.statusCode!=200||!apiResponse.success){
      throw Exception("Login failure. ${apiResponse.message}");
    }
    return apiResponse.data;
  }
}