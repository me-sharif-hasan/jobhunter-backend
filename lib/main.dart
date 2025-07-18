import 'dart:convert';
import 'dart:developer';

import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:personalized_job_hunter/features/job/controller/job_timeline_controller.dart';
import 'package:personalized_job_hunter/features/personalizedsite/controller/personal_site_controller.dart';
import 'package:personalized_job_hunter/features/personalizedsite/domain/datasource/personal_site_datasource.dart';
import 'package:personalized_job_hunter/util/firebase/firebase_analytics.dart';
import 'package:personalized_job_hunter/util/firebase/firebase_util.dart';
import 'package:personalized_job_hunter/util/http/client.dart';
import 'package:provider/provider.dart';

import 'features/auth/controller/auth_controller.dart';
import 'features/auth/controller/user_login_controller.dart';
import 'features/auth/controller/user_registration_controller.dart';
import 'package:get_it/get_it.dart';

import 'features/auth/domain/datasource/auth_datasource.dart';
import 'features/common/controller/meta_controller.dart';
import 'features/common/domain/datasource/backend_meta_datasource.dart';
import 'features/job/domain/datasource/job_datasource.dart';
import 'features/notification/controller/in_app_notification_controller.dart';
import 'features/notification/domain/datasource/notification_datasource.dart';
import 'features/profile/controller/facebook_controller.dart';
import 'features/profile/datasource/profile_datasource.dart';
import 'features/profile/controller/resume_upload_controller.dart';
import 'features/profile/datasource/resume_upload_datasource.dart';
import 'features/resume_strength/controller/resume_strength_controller.dart';
import 'features/resume_strength/domain/datasource/resume_strength_datasource.dart';
import 'features/splash/screens/splash_screen.dart';
import 'features/subscriptions/controller/site_controller.dart';
import 'features/subscriptions/domain/datasource/site_datasource.dart';
import 'firebase_options.dart';

GetIt locator = GetIt.instance;
final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();
String notificationPayload = "";

void setupLocator() {
  locator.registerSingleton<BackendClient>(BackendClient("token"));
  locator.registerSingleton<JobDatasource>(JobDatasource());
  locator.registerSingleton<SiteDataSource>(SiteDataSource());
  locator.registerSingleton<AuthDatasource>(AuthDatasource());
  locator.registerSingleton<ProfileDatasource>(ProfileDatasource());
  locator.registerSingleton<BackendMetaDatasource>(BackendMetaDatasource());
  locator.registerSingleton<NotificationDatasource>(NotificationDatasource());
  locator.registerSingleton<PersonalSiteDatasource>(PersonalSiteDatasource());
  locator.registerSingleton<ResumeStrengthDataSource>(ResumeStrengthDataSource());
  locator.registerSingleton<ResumeUploadDataSource>(ResumeUploadDataSource());
}


_getPlatform(){
  if(kIsWeb){
    return const FirebaseOptions(
      apiKey: "AIzaSyBGnoSfEUJHKk35JyBSH44VsBDFY0dlWQw",
      authDomain: "session-1-46a86.firebaseapp.com",
      databaseURL: "https://session-1-46a86-default-rtdb.firebaseio.com/",
      projectId: "session-1-46a86",
      storageBucket: "session-1-46a86.firebasestorage.app",
      messagingSenderId: "742827419889",
      appId: "1:742827419889:web:23434e6ea6afb425bf2150",
      measurementId: "G-TTY3BVV8JG"
      );
  }else{
    return DefaultFirebaseOptions.currentPlatform;
  }
}

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  if(Firebase.apps.isNotEmpty){
    log("Firebase already initialized");
  }else{
    try{
      await Firebase.initializeApp(
        options: _getPlatform(),
      );
    } catch (e) {
      log("Firebase initialization error: $e");
    }
  }
  
  setupLocator();

  initFlutterLocalNotification();
  requestPushNotificationPermission();
  listenForNotification();
  FirebaseMessaging.onBackgroundMessage(firebaseMessagingBackgroundHandler);
  RemoteMessage? remoteMessage = await FirebaseMessaging.instance.getInitialMessage();
  log("Remote message: $remoteMessage");

  final NotificationAppLaunchDetails? notificationAppLaunchDetails =
  await flutterLocalNotificationsPlugin.getNotificationAppLaunchDetails();

  if(notificationAppLaunchDetails?.didNotificationLaunchApp??false){
    try{
      log("iif: Notification clicked: ${notificationAppLaunchDetails?.notificationResponse?.payload}");
      notificationPayload=notificationAppLaunchDetails?.notificationResponse?.payload??"";
      log("iif4: Notification clicked: $notificationPayload");
    }catch(e) {
      log("iif-e-4: Notification clicked: $e");
    }
  }
  log("iif5: Notification clicked: $notificationPayload");

  if(remoteMessage!=null){
    notificationPayload = jsonEncode(remoteMessage.data);
  }

  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => UserRegistrationController()),
        ChangeNotifierProvider(create: (_) => UserLoginController()),
        ChangeNotifierProvider(create: (_) => JobTimelineController()),
        ChangeNotifierProvider(create: (_) => SiteController()),
        ChangeNotifierProvider(create: (_) => MetaController()),
        ChangeNotifierProvider(create: (_) => AuthController()),
        ChangeNotifierProvider(create: (_) => FacebookController()),
        ChangeNotifierProvider(create: (_) => InAppNotificationController()),
        ChangeNotifierProvider(create: (_)=> Personalsitecontroller()),
        ChangeNotifierProvider(create: (_) => ResumeStrengthController()),
        ChangeNotifierProvider(create: (_) => ResumeUploadController()),
      ],
      child: const JobHunter(),
    ),
  );
}

class JobHunter extends StatelessWidget {
  const JobHunter({super.key});

  @override
  Widget build(BuildContext context) {
    log("Building app: $notificationPayload");
    if(notificationPayload.isNotEmpty){
      handleAppOpeningThroughNotification(notificationPayload,context);
      notificationPayload="";
    }

    return MaterialApp(
      navigatorKey: navigatorKey,
      navigatorObservers: <NavigatorObserver>[
        FirebaseAnalyticsService().getAnalyticsObserver(),
      ],
      title: 'Job Hunter',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: SplashScreen(
        initialPayload: notificationPayload,
      ),
    );
  }

}

Future<void> initFlutterLocalNotification() async {
  const AndroidInitializationSettings androidSettings = AndroidInitializationSettings('@mipmap/ic_launcher');
  const InitializationSettings initializationSettings = InitializationSettings(android: androidSettings);
  await flutterLocalNotificationsPlugin.initialize(
    initializationSettings,
    onDidReceiveBackgroundNotificationResponse: handleBackgroundNotification,
    onDidReceiveNotificationResponse: (NotificationResponse response) async {
      log("iif3: Notification clicked: ${response.payload}");
      if(response.payload!=null){
        log("iif2: Notification clicked: ${response.payload}");
        notificationPayload=response.payload??"";
        MetaController.notificationPayload=jsonDecode(notificationPayload);
        //trigger refresh
        if(MetaController.mainPageBuildContext!=null){
          Navigator.of(MetaController.mainPageBuildContext!).pushReplacement(MaterialPageRoute(builder: (context) => SplashScreen(initialPayload: notificationPayload,)));
        }
      }
    },
  );
}

@pragma('vm:entry-point')
handleBackgroundNotification(NotificationResponse message) {
  log("iif: Notification clicked bg: ${message.payload}");
  notificationPayload=message.payload??"{id:-1}";
  MetaController.notificationPayload=jsonDecode(notificationPayload);
}

handleForegroundNotification(){}

