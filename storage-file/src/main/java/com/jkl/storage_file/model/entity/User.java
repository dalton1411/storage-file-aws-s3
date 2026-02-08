package com.jkl.storage_file.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String profileImageUrl;


    public User(String profileImageUrl, String name) {
        this.profileImageUrl = profileImageUrl;
        this.name = name;
    }

}
