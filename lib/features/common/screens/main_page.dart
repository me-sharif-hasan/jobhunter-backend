import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:personalized_job_hunter/features/common/widgets/bottom_navigation_bar.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';
import 'package:provider/provider.dart';

import '../../job/screens/job_timelime_screen.dart';
import '../../profile/screens/user_profile_screen.dart';
import '../../subscriptions/screens/company_subscription_screen.dart';

class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  @override
  void initState() {
    super.initState();
    Provider.of<MetaController>(context,listen: false).getFcmPushToken();
  }
  @override
  Widget build(BuildContext context) {
    MetaController.mainPageBuildContext = context;
    return Consumer<MetaController>(
      builder: (context,controller,_){
        return Scaffold(
          appBar: AppBar(
            title: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  controller.currentScreenTitle,
                  style: const TextStyle(
                    fontSize: 20,
                  ),
                ),
                const SizedBox(height: 4,),
                controller.loadingData ? LinearProgressIndicator(
                  valueColor: const AlwaysStoppedAnimation<Color>(Colors.white),
                  backgroundColor: Colors.white.withOpacity(0.5),
                ) : const SizedBox(),
              ],
            ),
            backgroundColor: Color(Constants.themeColor[controller.currentPage][0]), // Vibrant orange
            foregroundColor: Colors.white,
          ),
          body: IndexedStack(
            index: controller.currentPage,
            children: controller.screens,
          ),
          bottomNavigationBar: BottomNavigationBarWidget(
            selectedIndex: 1,
            onItemTapped: (int index) {
              Provider.of<MetaController>(context, listen: false).currentPage=index;
            },
          ),
        );
      },
    );
  }
}
