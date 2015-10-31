package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.Telephone;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Telephone entity.
 */
public interface TelephoneRepository extends JpaRepository<Telephone,Long> {

}
