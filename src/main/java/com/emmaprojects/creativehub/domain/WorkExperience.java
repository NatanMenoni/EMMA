package com.emmaprojects.creativehub.domain;

import java.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WorkExperience.
 */
@Entity
@Table(name = "work_experience")
public class WorkExperience implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "starting_date")
    private LocalDate startingDate;

    @Column(name = "finishing_date")
    private LocalDate finishingDate;

    @Column(name = "organization")
    private String organization;

    @Column(name = "title")
    private String title;

    @Column(name = "location")
    private String location;

    @Column(name = "current")
    private Boolean current;

    @Column(name = "job_description")
    private String jobDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDate getFinishingDate() {
        return finishingDate;
    }

    public void setFinishingDate(LocalDate finishingDate) {
        this.finishingDate = finishingDate;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkExperience workExperience = (WorkExperience) o;

        if ( ! Objects.equals(id, workExperience.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkExperience{" +
            "id=" + id +
            ", startingDate='" + startingDate + "'" +
            ", finishingDate='" + finishingDate + "'" +
            ", organization='" + organization + "'" +
            ", title='" + title + "'" +
            ", location='" + location + "'" +
            ", current='" + current + "'" +
            ", jobDescription='" + jobDescription + "'" +
            '}';
    }
}
