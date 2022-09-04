package com.potato.instock.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@EnableSwagger2
public class InitSupport {

    @Bean("firebaseApp")
    void initFirebase() throws IOException {
        FileInputStream refreshToken = new FileInputStream("src/main/resources/in-stock-tracker-59cba-firebase-adminsdk-x73c4-ed73e36fe1.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(refreshToken))
                .setDatabaseUrl("https://in-stock-tracker-59cba-default-rtdb.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.potato.instock")).build();
    }

}
