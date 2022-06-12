package com.example.sweater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.sweater")
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
//        application.setDefaultProperties(Collections.singletonMap("server.port", "8085"));
        application.run(args);
    }

}