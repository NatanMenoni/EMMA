package com.emmaprojects.creativehub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "linkedin_link")
    private String linkedinLink;

    @Column(name = "profile_picture_path")
    private String profilePicturePath;

    @Column(name = "cover_picture_path")
    private String coverPicturePath;

    @Column(name = "promotion_picture_path")
    private String promotionPicturePath;

    @OneToMany()
    @JsonIgnore
    private Set<WorkCollection> workCollections = new HashSet<>();

    @OneToOne()
    @JsonIgnore
    private User user;

    @OneToMany()
    @JsonIgnore
    private Set<ProfileEvent> profileEvents = new HashSet<>();

    @OneToMany()
    @JsonIgnore
    private Set<WorkExperience> workExperiences = new HashSet<>();

    @OneToMany()
    @JsonIgnore
    private Set<Education> educations = new HashSet<>();

    @OneToMany()
    @JsonIgnore
    private Set<Award> awards = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getLinkedinLink() {
        return linkedinLink;
    }

    public void setLinkedinLink(String linkedinLink) {
        this.linkedinLink = linkedinLink;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getCoverPicturePath() {
        return coverPicturePath;
    }

    public void setCoverPicturePath(String coverPicturePath) {
        this.coverPicturePath = coverPicturePath;
    }

    public String getPromotionPicturePath() {
        return promotionPicturePath;
    }

    public void setPromotionPicturePath(String promotionPicturePath) {
        this.promotionPicturePath = promotionPicturePath;
    }

    public Set<WorkCollection> getWorkCollections() {
        return workCollections;
    }

    public void setWorkCollections(Set<WorkCollection> workCollections) {
        this.workCollections = workCollections;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ProfileEvent> getProfileEvents() {
        return profileEvents;
    }

    public void setProfileEvents(Set<ProfileEvent> profileEvents) {
        this.profileEvents = profileEvents;
    }

    public Set<WorkExperience> getWorkExperiences() {
        return workExperiences;
    }

    public void setWorkExperiences(Set<WorkExperience> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public Set<Education> getEducations() {
        return educations;
    }

    public void setEducations(Set<Education> educations) {
        this.educations = educations;
    }

    public Set<Award> getAwards() {
        return awards;
    }

    public void setAwards(Set<Award> awards) {
        this.awards = awards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Profile profile = (Profile) o;

        if ( ! Objects.equals(id, profile.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id=" + id +
            ", aboutMe='" + aboutMe + "'" +
            ", facebookLink='" + facebookLink + "'" +
            ", linkedinLink='" + linkedinLink + "'" +
            ", profilePicturePath='" + profilePicturePath + "'" +
            ", coverPicturePath='" + coverPicturePath + "'" +
            ", promotionPicturePath='" + promotionPicturePath + "'" +
            '}';
    }
}
