package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.WorkCollection;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkCollection entity.
 */
public interface WorkCollectionRepository extends JpaRepository<WorkCollection,Long> {

}
