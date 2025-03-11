import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/profile/controller/facebook_controller.dart';
import 'package:provider/provider.dart';

class FacebookButtonWidget extends StatefulWidget {
  final VoidCallback onPressed;

  const FacebookButtonWidget({super.key, required this.onPressed});

  @override
  State<FacebookButtonWidget> createState() => _FacebookButtonWidgetState();
}

class _FacebookButtonWidgetState extends State<FacebookButtonWidget> {
  @override
  Widget build(BuildContext context) {
    return Consumer<FacebookController>(
      builder: (context,controller,_) {
        return ElevatedButton.icon(
          style: ElevatedButton.styleFrom(
            backgroundColor: const Color(0xFF1877F2),
            foregroundColor: Colors.white,
            padding: const EdgeInsets.symmetric(vertical: 12, horizontal: 20),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
          ),
          icon: const Icon(Icons.facebook, size: 20),
          label: const Text(
            "Connect with Facebook",
            style: TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
          ),
          onPressed: (){
            controller.connectWithFacebook().catchError(
              (error){
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(error.toString()),
                    backgroundColor: Colors.red,
                  ),
                );
              },
            );
          },
        );
      }
    );
  }
}