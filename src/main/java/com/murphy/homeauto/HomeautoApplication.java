package com.murphy.homeauto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HomeautoApplication {


    public static void main(String[] args) {
        SpringApplication.run(HomeautoApplication.class, args);
    }



}
