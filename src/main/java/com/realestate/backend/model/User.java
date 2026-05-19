package com.realestate.backend.model;

import jakarta.persistence.*;


// OOP CONCEPT: INHERITANCE
// User extends Person, so it inherits id, name, email,
// and phone from the Person class automatically.

@Entity
@Table(name = "users")
public class User extends Person {

    // OOP CONCEPT: ENCAPSULATION - all fields are private
    @Column(nullable = false)
    private String password;


    @Column
    private String role = "USER"; // default role


    // Constructors

    public User() {}

    public User(String name, String email, String phone,
                String password, String role) {
        super(name, email, phone); // calls Person constructor
        this.password = password;
        this.role = role;
    }


    // Getters and Setters (Encapsulation)

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
