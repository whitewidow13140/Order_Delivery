package com.demo.deliverytracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.demo")
public class DeliveryTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryTrackerApplication.class, args);
    }
}
