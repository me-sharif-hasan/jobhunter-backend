import 'package:get_it/get_it.dart';

import '../../../util/http/client.dart';
import '../../../util/values/constants.dart';
import '../../auth/domain/models/user_settings_model.dart';

class ProfileDatasource{
  GetIt locator = GetIt.instance;
  late BackendClient _client;

  ProfileDatasource(){
    _client = locator<BackendClient>();
  }

  Future connectFacebook(String token) async{
    UserSettings userSettings = UserSettings(settingType: "facebook", settingJson: {"accessToken":token});
    //send userSettings to backend
    _client.post(Constants.facebookConnect, body: userSettings.toJsonMap());
  }
}