import 'dart:developer';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:provider/provider.dart';

import '../domain/datasource/site_datasource.dart';
import '../domain/models/site_model.dart';

class SiteController extends ChangeNotifier {
  GetIt locator = GetIt.instance;
  SiteDataSource? _siteDataSource;
  List<Site> sites = [];
  bool _isLoading = false;
  Map <int, bool> subscriptionFunctionInProgressIndicator = {};

  SiteController() {
    _siteDataSource = locator<SiteDataSource>();
  }

  get isLoading => _isLoading;

  Future getSites({int currentPage=0,isSilent=false,searchQuery=""}) async {
    try{
      if(!isSilent){
        _isLoading = true;
        WidgetsBinding.instance.addPostFrameCallback((_) {
          notifyListeners();
        });
      }
      if(isSilent){
        if(MetaController.mainPageBuildContext!=null){
          Provider.of<MetaController>(MetaController.mainPageBuildContext!, listen: false).loadingData = true;
        }
      }
      List <Site> loadedSites = await _siteDataSource!.getSites(currentPage,searchQuery);
      log("iishantosites:$loadedSites");
      if(isSilent&&loadedSites.isNotEmpty){
        sites = [...sites, ...loadedSites];
      }else{
        sites = [...loadedSites];
      }
    }catch(e){
      if(!isSilent){
        sites = [];
      }
    }finally{
      _isLoading = false;
      if(isSilent) {
        if (MetaController.mainPageBuildContext != null) {
          Provider
              .of<MetaController>(
              MetaController.mainPageBuildContext!, listen: false)
              .loadingData = false;
        }
      }
      notifyListeners();
    }
  }

  Future subscribe(int siteId) async {
    try {
      log("iishantosubscribe:$siteId");
      subscriptionFunctionInProgressIndicator[siteId] = true;
      notifyListeners();
      await _siteDataSource!.subscribe(siteId);
      sites = sites.map((site) {
        if (site.id == siteId) {
          site.subscribed = true;
        }
        return site;
      }).toList();
      log("message");
      notifyListeners();
    } catch (e) {
      rethrow;
    }finally{
      subscriptionFunctionInProgressIndicator[siteId] = false;
      notifyListeners();
    }
  }
  Future unsubscribe(int siteId) async {
    try {
      log("iishantounsubscribe:$siteId");
      subscriptionFunctionInProgressIndicator[siteId] = true;
      notifyListeners();
      await _siteDataSource!.unsubscribe(siteId);
      sites = sites.map((site) {
        if (site.id == siteId) {
          site.subscribed = false;
        }
        return site;
      }).toList();
      log("message");
      notifyListeners();
    } catch (e) {
      rethrow;
    }finally{
      subscriptionFunctionInProgressIndicator[siteId] = false;
      notifyListeners();
    }
  }

  void setSiteFilter(int siteId) {}
}
