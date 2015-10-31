package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.WorkExperience;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkExperience entity.
 */
public interface WorkExperienceRepository extends JpaRepository<WorkExperience,Long> {

}
