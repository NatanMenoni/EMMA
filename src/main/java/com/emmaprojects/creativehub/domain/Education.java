package com.emmaprojects.creativehub.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.emmaprojects.creativehub.domain.enumeration.DegreeType;

/**
 * A Education.
 */
@Entity
@Table(name = "education")
public class Education implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "institution_name")
    private String institutionName;

    @Column(name = "starting_year")
    private Integer startingYear;

    @Column(name = "finishing_year")
    private Integer finishingYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree")
    private DegreeType degree;

    @Column(name = "degree_other_description")
    private String degreeOtherDescription;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @Column(name = "career_name")
    private String careerName;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Integer getStartingYear() {
        return startingYear;
    }

    public void setStartingYear(Integer startingYear) {
        this.startingYear = startingYear;
    }

    public Integer getFinishingYear() {
        return finishingYear;
    }

    public void setFinishingYear(Integer finishingYear) {
        this.finishingYear = finishingYear;
    }

    public DegreeType getDegree() {
        return degree;
    }

    public void setDegree(DegreeType degree) {
        this.degree = degree;
    }

    public String getDegreeOtherDescription() {
        return degreeOtherDescription;
    }

    public void setDegreeOtherDescription(String degreeOtherDescription) {
        this.degreeOtherDescription = degreeOtherDescription;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
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

        Education education = (Education) o;

        if ( ! Objects.equals(id, education.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Education{" +
            "id=" + id +
            ", institutionName='" + institutionName + "'" +
            ", startingYear='" + startingYear + "'" +
            ", finishingYear='" + finishingYear + "'" +
            ", degree='" + degree + "'" +
            ", degreeOtherDescription='" + degreeOtherDescription + "'" +
            ", fieldOfStudy='" + fieldOfStudy + "'" +
            ", careerName='" + careerName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
