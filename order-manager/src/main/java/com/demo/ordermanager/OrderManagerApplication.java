package com.demo.ordermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.demo")
public class OrderManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderManagerApplication.class, args);
    }
}
