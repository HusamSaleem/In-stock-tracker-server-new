package com.potato.instock.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {
    @Value("${spring.mail.username}")
    private String email;
    @Value("${spring.mail.password}")
    private String password;

    // Email configs
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private Integer port;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean tls;

    public NotificationConfig() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public boolean isAuth() {
        return auth;
    }

    public boolean isTls() {
        return tls;
    }
}
