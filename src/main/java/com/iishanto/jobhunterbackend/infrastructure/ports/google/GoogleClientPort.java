package com.iishanto.jobhunterbackend.infrastructure.ports.google;

import com.iishanto.jobhunterbackend.domain.adapter.GoogleClientAdapter;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.infrastructure.google.GoogleIdTokenVerifier;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class GoogleClientPort implements GoogleClientAdapter {
    GoogleIdTokenVerifier verifier;
    @Override
    public SimpleUserModel getUserFromIdToken(String idToken) {
        SimpleUserModel user = verifier.verifyIdToken(idToken);
        Optional.ofNullable(user).orElseThrow(() -> new IllegalArgumentException("Invalid idToken"));
        return user;
    }
}
