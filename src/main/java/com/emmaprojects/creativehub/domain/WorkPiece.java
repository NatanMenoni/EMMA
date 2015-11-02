package com.emmaprojects.creativehub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A WorkPiece.
 */
@Entity
@Table(name = "work_piece")
public class WorkPiece implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "year_of_creation")
    private Integer yearOfCreation;

    @Column(name = "input_date")
    private LocalDate inputDate;

    @ManyToMany
    @JsonIgnore
    private Set<WorkCollection> workCollections = new HashSet<>();

    @ManyToOne
    private Tag tags;

    @ManyToOne
    private User user;

    @ManyToMany()
    @JsonIgnore
    private Set<Category> categories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getYearOfCreation() {
        return yearOfCreation;
    }

    public void setYearOfCreation(Integer yearOfCreation) {
        this.yearOfCreation = yearOfCreation;
    }

    public LocalDate getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

    public Set<WorkCollection> getWorkCollections() {
        return workCollections;
    }

    public void setWorkCollections(Set<WorkCollection> workCollections) {
        this.workCollections = workCollections;
    }

    public Tag getTags() {
        return tags;
    }

    public void setTags(Tag tag) {
        this.tags = tag;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categorys) {
        this.categories = categorys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkPiece workPiece = (WorkPiece) o;

        if ( ! Objects.equals(id, workPiece.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkPiece{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", imagePath='" + imagePath + "'" +
            ", yearOfCreation='" + yearOfCreation + "'" +
            ", inputDate='" + inputDate + "'" +
            '}';
    }
}
