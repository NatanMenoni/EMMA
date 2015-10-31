package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.Profile;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Profile entity.
 */
public interface ProfileRepository extends JpaRepository<Profile,Long> {

}
