import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/domain/model/job_model.dart';
import 'dart:io' show Platform;

import '../../webview/inappwebview.dart';


class ApplyButton extends StatelessWidget {
  final Job job;
  String applyButtonText="";
  BorderRadius buttonBorder;
  Icon? icon;
  static const BorderRadius _defaultBorderRadius = BorderRadius.all(Radius.circular(8));
  ApplyButton({super.key,required this.job,this.applyButtonText="Apply", this.buttonBorder=_defaultBorderRadius, this.icon});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(left: 12.0),
      child: icon==null?ElevatedButton(
        onPressed: () {
          launchBrowser(context,job.jobUrl??job.jobApplyLink!);
        },
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
          backgroundColor: Colors.white,
          foregroundColor: const Color(0xFFFFA726),
          shape: RoundedRectangleBorder(
            borderRadius: this.buttonBorder,
          ),
          elevation: 0,
          textStyle: const TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.w600,
          ),
        ).copyWith(
          overlayColor: MaterialStateProperty.all(
            const Color(0xFFFFA726).withOpacity(0.1),
          ),
        ),
        child: Text(applyButtonText),
      ):ElevatedButton.icon(
        onPressed: () {
          launchBrowser(context,job.jobUrl??job.jobApplyLink!);
        },
        style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
          backgroundColor: Colors.white,
          foregroundColor: icon!.color,
          shape: RoundedRectangleBorder(
            borderRadius: this.buttonBorder,
          ),
          elevation: 0,
          textStyle: const TextStyle(
            fontSize: 14,
          ),
        ).copyWith(
          overlayColor: MaterialStateProperty.all(
            const Color(0xFFFFA726).withOpacity(0.1),
          ),
        ),
        icon: icon!,
        label: Text(applyButtonText),
      ),
    );
  }

  void launchBrowser(BuildContext context,String url) {
    if(!kIsWeb&&(Platform.isAndroid || Platform.isIOS)){
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => JobWebViewScreen(job: job,url: url,),
        ),
      );
    }else{
      if(kIsWeb){
        // js.context.callMethod('open', [url]);
      }else{
        // launchUrl(
        //     Uri.parse(url)
        // );
      }
    }

    print('Launching $url');
  }
}
