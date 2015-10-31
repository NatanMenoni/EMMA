package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.Award;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Award entity.
 */
public interface AwardRepository extends JpaRepository<Award,Long> {

}
