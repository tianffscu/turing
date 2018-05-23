package com.scu.turing.entity;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(targetEntity = Role.class)
    private Role role;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String userName;
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
