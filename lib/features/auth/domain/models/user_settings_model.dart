class UserSettings{
  String settingType;
  Map <String,dynamic> settingJson;
  UserSettings({required this.settingType, required this.settingJson});

  Map<String, dynamic> toJsonMap() {
    return {
      'settingType': settingType,
      'settingJson': settingJson,
    };
  }
}