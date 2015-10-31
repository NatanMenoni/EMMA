package com.emmaprojects.creativehub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "level")
    private Integer level;

    @Column(name = "image_path")
    private String imagePath;

    @ManyToMany    @JoinTable(name = "category_sub_categories",
               joinColumns = @JoinColumn(name="categorys_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="sub_categoriess_id", referencedColumnName="ID"))
    private Set<Category> subCategories = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private Set<WorkPiece> workPieces = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Set<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Set<Category> categorys) {
        this.subCategories = categorys;
    }

    public Set<WorkPiece> getWorkPieces() {
        return workPieces;
    }

    public void setWorkPieces(Set<WorkPiece> workPieces) {
        this.workPieces = workPieces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Category category = (Category) o;

        if ( ! Objects.equals(id, category.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Category{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", level='" + level + "'" +
            ", imagePath='" + imagePath + "'" +
            '}';
    }
}
