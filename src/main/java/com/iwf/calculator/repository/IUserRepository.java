package com.iwf.calculator.repository;

import com.iwf.calculator.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUsername(String username);
}
