package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.WorkPiece;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkPiece entity.
 */
public interface WorkPieceRepository extends JpaRepository<WorkPiece,Long> {

    @Query("select workPiece from WorkPiece workPiece where workPiece.user.login = ?#{principal.username}")
    List<WorkPiece> findByUserIsCurrentUser();

}
