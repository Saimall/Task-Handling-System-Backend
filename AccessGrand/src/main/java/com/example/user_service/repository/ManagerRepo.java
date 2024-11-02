package com.example.user_service.repository;

import com.example.user_service.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepo extends JpaRepository<Manager,Long> {

    boolean existsByEmail(String email);

    Manager findByEmail(String email);
}
