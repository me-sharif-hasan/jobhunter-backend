package com.iishanto.jobhunterbackend.infrastructure.google;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Component
public class GoogleIdTokenVerifier {
    private static final String ENDPOINT = "https://oauth2.googleapis.com/tokeninfo?id_token=";
    private final RestClient restClient=RestClient.create();

    @Value("#{'${google.auth.api_client_ids}'.split(',')}")
    private List<String> clientIds;

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
        boolean  isAzpValid= clientIds.stream().map(id -> id.equals(azp)).reduce(Boolean::logicalOr).orElse(false);
        if (!isAzpValid) {
            System.out.println(';'+azp+" != "+this.clientIds +';');
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
