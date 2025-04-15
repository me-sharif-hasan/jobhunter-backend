import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/controller/meta_controller.dart';
import 'package:personalized_job_hunter/util/values/constants.dart';
import 'package:provider/provider.dart';

class BottomNavigationBarWidget extends StatefulWidget {
  const BottomNavigationBarWidget({super.key});

  @override
  State<BottomNavigationBarWidget> createState() =>
      _BottomNavigationBarWidgetState();
}

class _BottomNavigationBarWidgetState extends State<BottomNavigationBarWidget> {
  void _onItemTapped(int index) {
    Provider.of<MetaController>(context, listen: false).currentPage = index;
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<MetaController>(
      builder: (context,controller,_) {
        int selectedIndex=controller.currentPage;
        return Container(
          height: 70, // Slightly taller for shadow and gradient visibility
          decoration: BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
              colors: [
                Color(Constants.themeColor[selectedIndex][0]), // Orange
                Color(Constants.themeColor[selectedIndex][1]), // Soft peach
              ],
            ),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withOpacity(0.1),
                blurRadius: 10,
                offset: const Offset(0, -2),
              ),
            ],
          ),
          child: CurvedNavigationBar(
            height: 60,
            backgroundColor: Colors.transparent,
            // Let Container gradient show through
            color: Colors.white.withOpacity(0.2),
            // Subtle base color for items
            buttonBackgroundColor: Colors.white.withOpacity(0.9),
            // Selected item highlight
            animationDuration: const Duration(milliseconds: 300),
            animationCurve: Curves.easeInOut,
            items: [
              Icon(
                Icons.person,
                size: 30,
                color: selectedIndex == 0 ? const Color(0xFFFFA726) : Colors.white,
              ),
              Icon(
                Icons.timeline,
                size: 30,
                color: selectedIndex == 1 ? const Color(0xFFFFA726) : Colors.white,
              ),
              Icon(
                Icons.local_library_rounded,
                size: 30,
                color: selectedIndex == 2 ? const Color(0xFFFFA726) : Colors.white,
              ),
              Icon(
                Icons.notifications,
                size: 30,
                color: selectedIndex == 3 ? const Color(0xFFFFA726) : Colors.white,
              ),
              // Icon(
              //   Icons.settings,
              //   size: 30,
              //   color: selectedIndex == 4 ? const Color(0xFFFFA726) : Colors.white,
              // ),
            ],
            index: selectedIndex,
            onTap: _onItemTapped,
            letIndexChange: (index) => true,
          ),
        );
      }
    );
  }
}
