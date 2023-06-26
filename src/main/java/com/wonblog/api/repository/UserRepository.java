package com.wonblog.api.repository;

import com.wonblog.api.domain.User;
import com.wonblog.api.dto.request.Signup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);
}
