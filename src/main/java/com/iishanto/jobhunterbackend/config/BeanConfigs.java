package com.iishanto.jobhunterbackend.config;

import com.google.firebase.database.FirebaseDatabase;
import com.iishanto.jobhunterbackend.infrastructure.firebase.FirebaseHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Configuration
public class BeanConfigs {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
