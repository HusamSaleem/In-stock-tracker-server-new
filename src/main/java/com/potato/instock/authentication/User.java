package com.potato.instock.authentication;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private boolean notifyByEmail;
    @Column(nullable = false)
    private String uniqueIdentifier;

    public User() {
        this.uniqueIdentifier = UUID.randomUUID().toString();
    }

    public User(String email, String password, boolean notifyByEmail) {
        this.email = email;
        this.password = password;
        this.notifyByEmail = notifyByEmail;
        this.uniqueIdentifier = UUID.randomUUID().toString();
    }
}
