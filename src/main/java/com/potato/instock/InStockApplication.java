package com.potato.instock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(InStockApplication.class, args);
    }

}
