package com.iishanto.jobhunterbackend.infrastructure.ports.firebase;

import com.iishanto.jobhunterbackend.domain.adapter.NotificationAdapter;
import com.iishanto.jobhunterbackend.domain.adapter.UserDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleNotificationModel;
import com.iishanto.jobhunterbackend.infrastructure.database.*;
import com.iishanto.jobhunterbackend.infrastructure.firebase.FirebaseHandler;
import com.iishanto.jobhunterbackend.infrastructure.repository.JobsRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.NotificationRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.PushNotificationTokenRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class NotificationHandlerPort implements NotificationAdapter {
    JobsRepository jobsRepository;
    SubscriptionRepository subscriptionRepository;
    PushNotificationTokenRepository pushNotificationTokenRepository;
    FirebaseHandler firebaseHandler;
    NotificationRepository notificationRepository;
    UserDataAdapter userDataAdapter;

    @Override
    public void sendJobNotification(List<String> jobIds) {
        if(jobIds.isEmpty()) return;
        List <Opportunity> opportunityList =jobsRepository.findAllByJobIdIn(jobIds);
        Map < Site,List<Opportunity>> groupedJobs=new HashMap<>();
        for (Opportunity job: opportunityList){
            if(!groupedJobs.containsKey(job.getSite())){
                groupedJobs.put(job.getSite(),new LinkedList<>());
            }
            groupedJobs.get(job.getSite()).add(job);
        }
        for (Site site:groupedJobs.keySet()){
            List<Subscription> subscriptions=subscriptionRepository.findAllBySite(site);
            List < PushNotificationToken> pushNotificationTokens=processSubscription(subscriptions);
            if(pushNotificationTokens.isEmpty()) continue;
            firebaseHandler.sendPushNotification(
                    createNewJobNotificationPayload(groupedJobs.get(site),site,pushNotificationTokens)
            );
            for (PushNotificationToken token:pushNotificationTokens){
                User user=token.getUser();
                Notification notification=new Notification();
                notification.setTitle("New Job Openings available");
                notification.setBody("Checkout %d new job openings for %s.".formatted(groupedJobs.get(site).size(),site.getName()));
                notification.setIconUrl("https://www.google.com/s2/favicons?domain=%s&sz=64".formatted(site.getHomepage()));
                notification.setUser(user);
                notification.setResourceAction("site");
                notification.setResourceId(site.getId().toString());
                notificationRepository.save(notification);
            }
        }
    }

    @Override
    public List<SimpleNotificationModel> getInAppNotification(int page, int limit) {
        Pageable pageable=Pageable.ofSize(limit).withPage(page);
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userDataAdapter.getLoggedInUser().getId(),pageable).stream().map(Notification::toSimpleNotificationModel).toList();
    }

    private List < PushNotificationToken>  processSubscription(List<Subscription> subscriptions){
        List <User> targetUsers=subscriptions.stream().map(Subscription::getUser).toList();
        return pushNotificationTokenRepository.findAllByUserIn(targetUsers);
    }

    private FirebaseHandler.NotificationPayload createNewJobNotificationPayload(List <Opportunity> opportunityList, Site site, List < PushNotificationToken> pushNotificationTokens){
        String title="%d new job openings for %s.".formatted(opportunityList.size(),site.getName());
        String body="Check them out before they expires.\n";
        for(int i = 0; i< opportunityList.size(); i++){
            if(i>4){
                body+="and more.";
                break;
            }
            body+="%d. %s\n".formatted(i+1, opportunityList.get(i).getTitle());
        }
        List<String> targetTokens=pushNotificationTokens.stream().map(PushNotificationToken::getToken).toList();
        return FirebaseHandler.NotificationPayload.builder()
                .body(body)
                .title(title)
                .token(targetTokens)
                .id(site.getId())
                .iconUrl("https://www.google.com/s2/favicons?domain=%s&sz=64".formatted(site.getHomepage()))
                .build();
    }
}
