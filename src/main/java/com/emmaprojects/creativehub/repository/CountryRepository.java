package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.Country;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Country entity.
 */
public interface CountryRepository extends JpaRepository<Country,Long> {

}
