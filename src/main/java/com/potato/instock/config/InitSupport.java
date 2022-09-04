package com.potato.instock.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@EnableSwagger2
public class InitSupport {
    @Value("${GOOGLE_CREDENTIALS}")
    private String gservicesConfig;

    @Bean("firebaseApp")
    void initFirebase() throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject(gservicesConfig.toString());
        InputStream is = new ByteArrayInputStream(jsonObject.toString().getBytes());
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream((is)))
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
