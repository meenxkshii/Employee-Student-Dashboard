package com.cdac.studentportal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String course;
    private String username;
    private String password;

    public Student() {}

    public Student(String name, String course) {
        if (name.length() < 5) {
            throw new IllegalArgumentException("Name must be at least 5 characters long.");
        }
        this.name = name;
        this.course = course;
        generateCredentials();
    }

    
    @PrePersist
    public void generateCredentials() {
        if (this.username == null) {
            this.username = generateUsername(this.name);
        }
        if (this.password == null) {
            this.password = generatePassword();
        }
    }

    
    private String generateUsername(String name) {
        String prefix = name.length() >= 4 ? name.substring(0, 4) : name;
        return prefix.toLowerCase() + UUID.randomUUID().toString().substring(0, 4);
    }

   
    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    
    public void regenerateCredentials() {
        this.username = generateUsername(this.name);
        this.password = generatePassword();
    }

}
