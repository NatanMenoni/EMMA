package com.emmaprojects.creativehub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A WorkCollection.
 */
@Entity
@Table(name = "work_collection")
public class WorkCollection implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "cover_image_path")
    private String coverImagePath;

    @ManyToMany(mappedBy = "workCollections")
    @JsonIgnore
    private Set<WorkPiece> workPieces = new HashSet<>();

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
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

        WorkCollection workCollection = (WorkCollection) o;

        if ( ! Objects.equals(id, workCollection.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkCollection{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", date='" + date + "'" +
            ", coverImagePath='" + coverImagePath + "'" +
            '}';
    }
}
