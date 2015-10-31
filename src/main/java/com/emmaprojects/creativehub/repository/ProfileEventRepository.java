package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.ProfileEvent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProfileEvent entity.
 */
public interface ProfileEventRepository extends JpaRepository<ProfileEvent,Long> {

}
