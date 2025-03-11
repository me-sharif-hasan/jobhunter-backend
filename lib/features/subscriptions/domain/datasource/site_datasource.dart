import 'dart:convert';

import 'package:get_it/get_it.dart';
import 'package:http/http.dart';
import 'package:personalized_job_hunter/features/common/domain/model/api_response.dart';

import '../../../../util/http/client.dart';
import '../../../../util/values/constants.dart';
import '../models/site_model.dart';

class SiteDataSource {
  GetIt locator = GetIt.instance;
  BackendClient? client;

  SiteDataSource() {
    client = locator<BackendClient>();
  }

  Future<List<Site>> getSites(int page,String searchQuery) {
    int limit=10;
    return client!.get("${Constants.getSites}?page=$page&limit=$limit&query=$searchQuery").then((response) {
      if (response.statusCode == 200) {
        final ApiResponse data =
            ApiResponse.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
        List<Site> siteList = [];
        for (var site in data.data) {
          siteList.add(Site.fromJson(site));
        }
        return siteList;
      } else {
        throw Exception('Failed to load sites');
      }
    });
  }

  Future subscribe(int siteId) async {
    Response response =
        await client!.post(Constants.subscribe, body: {"site_id": siteId});
    ApiResponse apiResponse = ApiResponse.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
    if (!apiResponse.success) {
      throw Exception(apiResponse.message);
    }
    if (response.statusCode != 200) {
      throw Exception('Failed to subscribe, already subscribed?');
    }
  }

  Future unsubscribe(int siteId) async {
    Response response =
        await client!.post(Constants.unsubscribe, body: {"site_id": siteId});
    ApiResponse apiResponse = ApiResponse.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
    if (!apiResponse.success) {
      throw Exception(apiResponse.message);
    }
    if (response.statusCode != 200) {
      throw Exception('Failed to unsubscribe, already unsubscribed?');
    }
  }
}
