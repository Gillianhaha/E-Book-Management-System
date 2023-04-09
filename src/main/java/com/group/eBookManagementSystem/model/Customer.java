package com.group.eBookManagementSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Customer {

    //    @GeneratedValue(strategy = GenerationType.AUTO)
//    private String id;
    @Id
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    @ElementCollection
    private List<Integer> myLibrary;
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN,
        USER
    }

}
