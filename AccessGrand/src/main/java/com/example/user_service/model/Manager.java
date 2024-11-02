package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerId;
    private String name;
    private String email;
    private String contact;
    @OneToMany(mappedBy ="manager",cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
    @JsonManagedReference
    private List<Employee> employees;
}
