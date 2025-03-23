import 'dart:ui';

class Constants{
  static const String baseUrl = 'https://jobhunterbackend.gentlesmoke-d65a2350.westus2.azurecontainerapps.io';
  // static const String baseUrl = 'http://10.0.2.2:8080';
  static const String token = '';

  static const getJobs = '/api/jobs';
  static const getSites = '/api/site';
  static const subscribe = '/api/subscription';
  static const unsubscribe = '/api/subscription/remove';
  static const getCurrentUser = '/api/user';

  static const register = "/auth/google/registration";
  static const facebookConnect = "/auth/facebook/connect";
  static const saveFirebasePushToken = "/api/settings/saveFirebaseToken";

  static const List<List<int>> themeColor = [
    [0xFF42A5F5, 0xFF79BCFF], // Light Blue to Blue
    [0xFFFFA726, 0xFFFF963C], // Orange to Light Orange
    [0xFFE94BFC, 0xFFFF7416], // Light Green to Green
    [0xFFAB47BC, 0xFF8E24AA], // Light Purple to Purple
    [0xFFEC407A, 0xFFD81B60], // Pink to Deep Pink
    [0xFF7E57C2, 0xFF5E35B1], // Light Indigo to Indigo
    [0xFF26A69A, 0xFF00897B], // Teal to Deep Teal
    [0xFF78909C, 0xFF546E7A], // Blue Grey to Dark Blue Grey
  ];

  static List <Color> getThemeColor(int index) {
    List <Color> colors = [
      Color(themeColor[index][0]),
      Color(themeColor[index][1]),
    ];
    return colors;
  }
}