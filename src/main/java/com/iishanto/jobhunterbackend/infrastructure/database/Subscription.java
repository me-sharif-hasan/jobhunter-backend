package com.iishanto.jobhunterbackend.infrastructure.database;

import com.iishanto.jobhunterbackend.domain.model.SimpleSubscriptionModel;
import com.iishanto.jobhunterbackend.infrastructure.repository.SiteRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserRepository;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Optional;

@Entity
@Data
@ToString
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;
    private boolean isActive;
    public static Subscription fromSubscriptionModels(SimpleSubscriptionModel simpleSubscriptionModel){
        return fromSubscriptionModels(null,null,simpleSubscriptionModel);
    }

    public static Subscription fromSubscriptionModels(UserRepository userRepository,SiteRepository siteRepository, SimpleSubscriptionModel simpleSubscriptionModel){
        Subscription subscription=new Subscription();
        subscription.setId(simpleSubscriptionModel.getId());
        subscription.setSite(Site.fromSiteModel(simpleSubscriptionModel.getSite()));
        subscription.setActive(simpleSubscriptionModel.isActive());
        if(siteRepository!=null){
            Optional<Site> site=siteRepository.findById(simpleSubscriptionModel.getSite().getId());
            if(site.isPresent()){
                subscription.setSite(site.get());
            }else{
                throw new RuntimeException("Site not found");
            }
        }
        if(userRepository!=null){
            Optional<User> user=userRepository.findById(simpleSubscriptionModel.getUser().getId());
            if(user.isPresent()){
                subscription.setUser(user.get());
            }else{
                throw new RuntimeException("User not found");
            }
        }
        return subscription;
    }

    public SimpleSubscriptionModel toSubscriptionModel(){
        return SimpleSubscriptionModel.builder()
                .id(id)
                .site(site.toDomain())
                .user(user.toUserModel())
                .build();
    }
}
