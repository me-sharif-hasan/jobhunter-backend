package com.iishanto.jobhunterbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOriginPatterns("*") // Allow frontend domain
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow necessary methods
                        .allowedHeaders("*")
                        .allowCredentials(true); // Required if using cookies
            }
        };
    }
}
