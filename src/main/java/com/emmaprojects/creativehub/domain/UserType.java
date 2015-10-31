package com.emmaprojects.creativehub.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserType.
 */
@Entity
@Table(name = "user_type")
public class UserType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserType userType = (UserType) o;

        if ( ! Objects.equals(id, userType.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserType{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
