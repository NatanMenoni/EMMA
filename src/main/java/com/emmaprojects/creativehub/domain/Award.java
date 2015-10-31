package com.emmaprojects.creativehub.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Award.
 */
@Entity
@Table(name = "award")
public class Award implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "name")
    private String name;

    @Column(name = "issuer")
    private String issuer;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Award award = (Award) o;

        if ( ! Objects.equals(id, award.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Award{" +
            "id=" + id +
            ", year='" + year + "'" +
            ", name='" + name + "'" +
            ", issuer='" + issuer + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
