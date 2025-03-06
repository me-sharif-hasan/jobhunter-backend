package com.iishanto.jobhunterbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobHunterBackend {

    public static void main(String[] args) {
        SpringApplication.run(JobHunterBackend.class, args);
    }

}
