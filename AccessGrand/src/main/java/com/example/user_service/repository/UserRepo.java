package com.example.user_service.repository;

import com.example.user_service.model.Usermain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Usermain,Long> {

    void deleteByEmail(String email);

    Optional<Usermain> findByEmail(String email);
}
