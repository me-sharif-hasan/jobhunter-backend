import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/personalizedsite/domain/datasource/personal_site_datasource.dart';
import 'package:personalized_job_hunter/features/subscriptions/domain/models/site_model.dart';

class Personalsitecontroller extends ChangeNotifier{
  GetIt locator = GetIt.instance;
  List<Site> personalSites = [];
  int currentPage = 0;
  String searchQuery = "";
  bool isLoading = false;

  late PersonalSiteDatasource personalSiteDatasource;
  Personalsitecontroller(){
    personalSiteDatasource = locator<PersonalSiteDatasource>();
  }

  Future<void> loadPersonalSites(BuildContext context) async{
    log("Personalsitecontroller: loadPersonalSites");
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if(personalSites.isEmpty){
        isLoading = true;
        notifyListeners();
      }
    });
    try{
      personalSites = await personalSiteDatasource.loadPersonalSites(searchQuery, currentPage);
      WidgetsBinding.instance.addPostFrameCallback((_) {
          notifyListeners();
      });
    }finally{
      WidgetsBinding.instance.addPostFrameCallback((_) {
        isLoading = false;
        notifyListeners();
      });
    }
  }

  Future<Site?> addPersonalSite(String homepage, String jobListPageUrl) async {
    try {
      Site newSite = await personalSiteDatasource.addPersonalSite(homepage, jobListPageUrl);
      log("New personal site added: ${newSite.name}");
      // Add the new site to the beginning of the list
      personalSites.insert(0, newSite);
      notifyListeners();
      
      return newSite;
    } catch (e) {
      log("Error adding personal site: $e");
      rethrow;
    }
  }

  Future<void> refreshSites(BuildContext context) async {
    currentPage = 0;
    personalSites.clear();
    await loadPersonalSites(context);
  }

}