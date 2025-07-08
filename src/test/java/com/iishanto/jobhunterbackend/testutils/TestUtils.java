package com.iishanto.jobhunterbackend.testutils;

import com.iishanto.jobhunterbackend.config.security.JwtUtil;
import com.iishanto.jobhunterbackend.domain.model.SimpleUserModel;
import com.iishanto.jobhunterbackend.infrastructure.ports.database.UserDataPort;
import com.iishanto.jobhunterbackend.infrastructure.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MySQLContainer;

@Component
public class TestUtils {
    @Autowired
    UserDataPort userDataPort;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    UserAppliedJobsRepository userAppliedJobsRepository;
    @Autowired
    JobsRepository jobsRepository;
    @Autowired
    SiteRepository siteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired IndexingStrategyRepository indexingStrategyRepository;


    static final MySQLContainer MYSQL_CONTAINER;
    static {
        MYSQL_CONTAINER = new MySQLContainer("mysql:8.0.32");
        MYSQL_CONTAINER.start();
    }

    public static void setupMySQL(DynamicPropertyRegistry registry) {
        System.out.println("Setting up MySQL container properties");
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("log4j.logger.org.testcontainers", () -> "DEBUG");
        registry.add("google.gemini.apikey", () -> "TEST_GEMINI_API_KEY");
    }



    public String setupAuthentication() {
        SimpleUserModel user = SimpleUserModel.builder()
                .email("test@gmail.com")
                .name("Test")
                .token("test")
                .build();
        userDataPort.addUser(user);
        return generateToken();
    }

    String generateToken() {
        return jwtUtil.generateToken("test@gmail.com", "ROLE_USER");
    }

    public void clearDatabase() {
        subscriptionRepository.deleteAll();
        userAppliedJobsRepository.deleteAll();
        jobsRepository.deleteAll();
        indexingStrategyRepository.deleteAll();
        siteRepository.deleteAll();
        userRepository.deleteAll();
    }
}
