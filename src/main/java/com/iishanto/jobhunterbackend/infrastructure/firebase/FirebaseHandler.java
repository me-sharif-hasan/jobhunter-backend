package com.iishanto.jobhunterbackend.infrastructure.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class FirebaseHandler {
    public FirebaseHandler() {
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("credentials/jobhunter-service-account.json")) {
            if (serviceAccount == null) {
                throw new IllegalStateException("Firebase credentials file not found");
            }
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(firebaseOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPushNotification(NotificationPayload notificationPayload){
        if(notificationPayload.token.isEmpty()) return;
        System.out.printf("Sending push notification %s \n %s \n %d%n", notificationPayload.title,notificationPayload.body,notificationPayload.token.size());
        MulticastMessage multicastMessage = MulticastMessage.builder()
//                .setNotification(Notification.builder()
//                        .setTitle(notificationPayload.title)
//                        .setBody(notificationPayload.body)
//                        .setImage(notificationPayload.iconUrl)
//                        .build())
                .putData("title", notificationPayload.title)
                .putData("body", notificationPayload.body)
                .putData("id", notificationPayload.id.toString())
                .putData("icon", notificationPayload.iconUrl)
                .addAllTokens(notificationPayload.token)
                .build();
        try{
            BatchResponse response=FirebaseMessaging
                    .getInstance()
                    .sendEachForMulticast(multicastMessage);
            System.out.println(response.getSuccessCount()+" messages were sent successfully");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Builder
    public static class NotificationPayload{
        String title;
        String body;
        Long id;
        String iconUrl;
        List<String> token;
    }
}
