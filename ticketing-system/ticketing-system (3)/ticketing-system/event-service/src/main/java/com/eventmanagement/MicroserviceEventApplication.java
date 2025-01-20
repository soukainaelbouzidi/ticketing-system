package com.eventmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // Permet l'enregistrement du service auprès d'Eureka
public class MicroserviceEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceEventApplication.class, args);
    }
}