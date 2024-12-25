package com.alhas.airbnb.user.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "authority")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Size( max = 50)
    @Column(length = 50)
    private String name;


    public @NotNull @Size(max = 50) String getName() {
        return name;
    }

    public void setName(@NotNull @Size(max = 50) String name) {
        this.name = name;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return Objects.equals(name, authority.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Authority{" +
                "name='" + name + '\'' +
                '}';
    }
}
