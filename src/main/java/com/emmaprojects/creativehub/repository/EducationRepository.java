package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.Education;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Education entity.
 */
public interface EducationRepository extends JpaRepository<Education,Long> {

}
