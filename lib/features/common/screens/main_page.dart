import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:personalized_job_hunter/features/common/widgets/bottom_navigation_bar.dart';
import 'package:provider/provider.dart';


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
    Provider.of<MetaController>(context, listen: false).getJobAppliedOptions();
  }
  @override
  Widget build(BuildContext context) {
    MetaController.mainPageBuildContext = context;
    return Consumer<MetaController>(
      builder: (context,controller,_){
        return Scaffold(
          body: IndexedStack(
            index: controller.currentPage,
            children: controller.screens,
          ),
          bottomNavigationBar: const BottomNavigationBarWidget(),
        );
      },
    );
  }
}
