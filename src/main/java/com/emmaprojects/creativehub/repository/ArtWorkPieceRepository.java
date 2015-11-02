package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.ArtWorkPiece;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ArtWorkPiece entity.
 */
public interface ArtWorkPieceRepository extends JpaRepository<ArtWorkPiece,Long> {

}
