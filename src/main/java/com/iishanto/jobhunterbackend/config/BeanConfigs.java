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
    public WebDriver webDriver(){
//        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless"); // Ensure JS execution in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--enable-javascript");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--user-data-dir=/tmp/chrome-user-data-" + UUID.randomUUID());
        options.addArguments("--lang=en-US");
        options.setExperimentalOption("prefs", Map.of(
                "intl.accept_languages", "en-US"
        ));

        return new ChromeDriver(options);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
