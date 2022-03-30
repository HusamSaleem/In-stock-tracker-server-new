package com.potato.instock.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUniqueIdentifier(String uniqueIdentifier);
    List<User> findByNotifyByEmail(boolean notifyByEmail);
}
