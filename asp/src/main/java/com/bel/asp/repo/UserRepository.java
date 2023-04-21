package com.bel.asp.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bel.asp.entity.User;


public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByEmail(String email);
}
