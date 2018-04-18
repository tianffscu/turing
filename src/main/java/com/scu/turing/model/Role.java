package com.scu.turing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Role {

    @Id
    @Column(updatable = false, insertable = false)
    private int id;
    private String name;
    
    private static final Role SYS_ADMIN = new Role(0, "SysAdmin");
    private static final Role SYS_USER = new Role(1, "SysUser");

    public Role() {
    }

    Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Role getAdmin() {
        return SYS_ADMIN;
    }

    public static Role getSysUser() {
        return SYS_USER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id &&
                Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
