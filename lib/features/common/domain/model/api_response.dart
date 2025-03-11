
import 'dart:convert';

import 'package:http/http.dart';

class ApiResponse{
  final bool success;
  final String message;
  final dynamic data;

  ApiResponse({required this.success, required this.message, required this.data});

  factory ApiResponse.fromJson(Map<String, dynamic> json){
    return ApiResponse(
      success: json['success'],
      message: json['message'],
      data: json['data']
    );
  }

  factory ApiResponse.fromResponseUtf8(Response response){
    Map <String,dynamic> json=jsonDecode(utf8.decode(response.bodyBytes));
    return ApiResponse(
        success: json['success'],
        message: json['message'],
        data: json['data']
    );
  }
}