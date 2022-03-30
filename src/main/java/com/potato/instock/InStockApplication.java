package com.potato.instock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class InStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(InStockApplication.class, args);
    }

}
