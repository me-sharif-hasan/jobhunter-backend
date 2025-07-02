import 'dart:developer';

import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/domain/model/api_response.dart';
import 'package:personalized_job_hunter/features/subscriptions/domain/models/site_model.dart';
import 'package:personalized_job_hunter/util/http/client.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';

class PersonalSiteDatasource {
  final GetIt locator = GetIt.instance;
  BackendClient get backendClient => locator<BackendClient>();
  Future<List<Site>> loadPersonalSites(String query,int page) {
    String queryParam = "?query=$query&limit=10&page=$page";
    String path = Constants.getPersonalSites + queryParam;
    return backendClient.get(path).then((response) {
      if (response.statusCode == 200) {
        ApiResponse apiResponse = ApiResponse.fromResponseUtf8(response);
        List<dynamic> sitesData = apiResponse.data;
        List<Site> personalSites = sitesData.map((site) => Site.fromJson(site)).toList();
        return personalSites;
      } else {
        // Handle error response
        throw Exception('Failed to load personal sites');
      }
    }).catchError((error) {
      // Handle any errors that occur during the request
      throw Exception('Error fetching personal sites: $error');
    });
  }
  
  Future<Site> addPersonalSite(String homepage, String jobListPageUrl) {
    String path = Constants.addPersonalSite;
    
    Map<String, dynamic> requestBody = {
      'homepage': homepage,
      'jobListPageUrl': jobListPageUrl,
    };
    log("Adding personal site with homepage: $homepage and jobListPageUrl: $jobListPageUrl");
    return backendClient.post(path, body: requestBody).then((response) {
      log("Response status code: ${response.statusCode}");
      if (response.statusCode == 200) {
        ApiResponse apiResponse = ApiResponse.fromResponseUtf8(response);
        Site newSite = Site.fromJson(apiResponse.data);
        return newSite;
      } else {
        // Handle error response
        String errorMessage = 'Failed to add personal site';
        try {
          ApiResponse errorResponse = ApiResponse.fromResponseUtf8(response);
          errorMessage = errorResponse.message;
        } catch (e) {
          // Use default error message if parsing fails
        }
        throw Exception(errorMessage);
      }
    }).catchError((error) {
      // Handle any errors that occur during the request
      throw Exception('Error adding personal site: $error');
    });
  }
  
}