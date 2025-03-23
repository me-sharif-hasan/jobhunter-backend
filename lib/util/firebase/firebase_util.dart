import 'dart:convert';
import 'dart:developer';
import 'dart:typed_data';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:http/http.dart';
import 'package:provider/provider.dart';

import '../../features/job/controller/job_timeline_controller.dart';

final FlutterLocalNotificationsPlugin flutterLocalNotificationsPlugin = FlutterLocalNotificationsPlugin();

@pragma('vm:entry-point')
Future<void> firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  log("iif: Firebase background message received ${message.data.toString()} ${message.data['id']}");
  await Firebase.initializeApp();
  showNotification(
    int.parse(message.data['id']),
    message.data['title'] ?? "No Title",
    message.data['body'] ?? "No Body",
    message.data['icon'] ?? "",
  );
}

void requestPushNotificationPermission() async {
  NotificationSettings settings = await FirebaseMessaging.instance.getNotificationSettings();

  if (settings.authorizationStatus == AuthorizationStatus.notDetermined) {
    await FirebaseMessaging.instance.requestPermission(
      alert: true,
      announcement: false,
      badge: true,
      carPlay: false,
      criticalAlert: false,
      provisional: false,
      sound: true,
    );
  } else {
    log("Permission already granted");
  }
}

void listenForNotification() {
  FirebaseMessaging.onMessage.listen((RemoteMessage message) {
    log('iif: Got a message while in the foreground!');
    log('Message data: ${message.data}');
    log('Message also contained a notification: ${message.notification}');
    showNotification(
      int.parse(message.data['id']??"0"),
      message.data['title'] ?? "No Title",
      message.data['body'] ?? "No Body",
      message.data['icon'] ?? "",
    );
  });
}

Future<void> showNotification(int id, String title, String body, String iconUrl) async {
  log('iif: Showing notification with id: $id, title: $title, body: $body, iconUrl: $iconUrl');

  ByteArrayAndroidBitmap? smallIcon;
  if (iconUrl.isNotEmpty) {
    try {
      final Response response = await get(Uri.parse(iconUrl));
      final Uint8List bytes = response.bodyBytes;
      smallIcon = ByteArrayAndroidBitmap(bytes);
    } catch (e) {
      log("Error loading icon: $e");
    }
  }

  AndroidNotificationDetails androidPlatformChannelSpecifics = AndroidNotificationDetails(
    'default_channel_id',
    'Default Channel',
    channelDescription: 'This is the default notification channel.',
    importance: Importance.high,
    priority: Priority.high,
    ticker: 'ticker',
    icon: '@mipmap/ic_launcher',
    largeIcon: smallIcon, // Set the downloaded image as a small icon
    styleInformation: BigTextStyleInformation(body), // Ensure full text is shown
  );

  NotificationDetails platformChannelSpecifics = NotificationDetails(android: androidPlatformChannelSpecifics);
  int currentTime = DateTime.now().millisecondsSinceEpoch;
  log("iif: Current time: $currentTime");
  await flutterLocalNotificationsPlugin.show(
    id,
    title,
    body,
    platformChannelSpecifics,
    payload: "{\"type\": \"site_id\", \"id\": $id}"
  );
}


void handleAppOpeningThroughNotification(String payload,BuildContext context) {
  log('iif: A new onMessageOpenedApp event was published!');
  Map<String,dynamic> data = jsonDecode(payload);
  log('iif: Message data: ${data}');
  int siteId = data['id'];
  Provider.of<JobTimelineController>(context, listen: false).setSiteFilter(siteId);
}