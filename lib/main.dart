import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:personalized_job_hunter/features/common/screens/main_page.dart';
import 'package:personalized_job_hunter/features/job/controller/job_timeline_controller.dart';
import 'package:personalized_job_hunter/features/job/screens/job_timelime_screen.dart';
import 'package:personalized_job_hunter/util/http/client.dart';
import 'package:provider/provider.dart';

import 'features/auth/controller/auth_controller.dart';
import 'features/auth/controller/user_login_controller.dart';
import 'features/auth/controller/user_registration_controller.dart';
import 'package:get_it/get_it.dart';

import 'features/auth/domain/datasource/auth_datasource.dart';
import 'features/common/controller/meta_controller.dart';
import 'features/job/domain/datasource/job_datasource.dart';
import 'features/profile/controller/facebook_controller.dart';
import 'features/profile/datasource/profile_datasource.dart';
import 'features/splash/screens/splash_screen.dart';
import 'features/subscriptions/controller/site_controller.dart';
import 'features/subscriptions/domain/datasource/site_datasource.dart';
import 'firebase_options.dart';

GetIt locator = GetIt.instance;
final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

void setupLocator() {
  locator.registerSingleton<BackendClient>(BackendClient("token"));
  locator.registerSingleton<JobDatasource>(JobDatasource());
  locator.registerSingleton<SiteDataSource>(SiteDataSource());
  locator.registerSingleton<AuthDatasource>(AuthDatasource());
  locator.registerSingleton<ProfileDatasource>(ProfileDatasource());
}

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  setupLocator();
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
      ],
      child: const JobHunter(),
    ),
  );
}

class JobHunter extends StatelessWidget {
  const JobHunter({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      navigatorKey: navigatorKey,
      title: 'Job Hunter',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const SplashScreen(),
    );
  }
}
