package com.quadrangle.capstone.pill_navigator.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ERole role;

    public Role() {}
    public Role(ERole role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public ERole getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRole(ERole role) {
        this.role = role;
    }
}
