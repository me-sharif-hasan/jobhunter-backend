package com.iishanto.jobhunterbackend.infrastructure.ports.settings;

import com.iishanto.jobhunterbackend.domain.adapter.SettingsDataAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.infrastructure.database.PushNotificationToken;
import com.iishanto.jobhunterbackend.infrastructure.database.User;
import com.iishanto.jobhunterbackend.infrastructure.ports.database.UserDataPort;
import com.iishanto.jobhunterbackend.infrastructure.repository.PushNotificationTokenRepository;
import com.iishanto.jobhunterbackend.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class SettingDataPort implements SettingsDataAdapter {
    PushNotificationTokenRepository pushNotificationTokenRepository;
    UserRepository userRepository;
    UserDataPort userDataPort;
    @Override
    public void saveNotificationToken(String token, String providerName) {
        PushNotificationToken pushNotificationToken=new PushNotificationToken();
        pushNotificationToken.setToken(token);
        SimpleUserModel simpleUserModel=userDataPort.getLoggedInUser();
        Optional<User> user=userRepository.findById(simpleUserModel.getId());
        if(user.isPresent()){
            pushNotificationToken.setUser(user.get());
            pushNotificationToken.setProviderName(providerName);
            pushNotificationToken.setActive(true);
            pushNotificationTokenRepository.save(pushNotificationToken);
        }else{
            throw new RuntimeException("User not found");
        }
    }
}
