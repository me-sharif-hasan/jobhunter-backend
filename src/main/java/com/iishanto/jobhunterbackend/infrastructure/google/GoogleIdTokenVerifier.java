package com.iishanto.jobhunterbackend.infrastructure.google;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;

@Component
public class GoogleIdTokenVerifier {
    private static final String ENDPOINT = "https://oauth2.googleapis.com/tokeninfo?id_token=";
    private final RestClient restClient=RestClient.create();

    @Value("${google.auth.api_client_id}")
    private String clientId;

    private String getTokenInfoJson(String idToken) {
        String url = ENDPOINT + idToken;
        return restClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .header("Accept", "application/json")
                .retrieve()
                .body(String.class);
    }

    public SimpleUserModel verifyIdToken(String idToken) {
        String tokenInfoJson = getTokenInfoJson(idToken);
        JsonObject jsonObject = JsonParser.parseString(tokenInfoJson).getAsJsonObject();
        String email = jsonObject.get("email").getAsString();
        String azp = jsonObject.get("azp").getAsString();
        String name = jsonObject.get("name").getAsString();
        String picture = jsonObject.get("picture").getAsString();
        boolean emailVerified = jsonObject.get("email_verified").getAsBoolean();
        if(!emailVerified) return null;
        if(!azp.equals(this.clientId)) {
            System.out.println(';'+azp+" != "+this.clientId+';');
            return null;
        }

        return SimpleUserModel.builder()
                .email(email)
                .name(name)
                .imageUrl(picture)
                .token(idToken)
                .lastLogin(Timestamp.from(Instant.now()))
                .build();
    }
}
