package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.WorkPiece;
import com.emmaprojects.creativehub.repository.WorkPieceRepository;
import com.emmaprojects.creativehub.web.rest.util.HeaderUtil;
import com.emmaprojects.creativehub.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WorkPiece.
 */
@RestController
@RequestMapping("/api")
public class WorkPieceResource {

    private final Logger log = LoggerFactory.getLogger(WorkPieceResource.class);

    @Inject
    private WorkPieceRepository workPieceRepository;

    /**
     * POST  /workPieces -> Create a new workPiece.
     */
    @RequestMapping(value = "/workPieces",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkPiece> createWorkPiece(@RequestBody WorkPiece workPiece) throws URISyntaxException {
        log.debug("REST request to save WorkPiece : {}", workPiece);
        if (workPiece.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new workPiece cannot already have an ID").body(null);
        }
        WorkPiece result = workPieceRepository.save(workPiece);
        return ResponseEntity.created(new URI("/api/workPieces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workPiece", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workPieces -> Updates an existing workPiece.
     */
    @RequestMapping(value = "/workPieces",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkPiece> updateWorkPiece(@RequestBody WorkPiece workPiece) throws URISyntaxException {
        log.debug("REST request to update WorkPiece : {}", workPiece);
        if (workPiece.getId() == null) {
            return createWorkPiece(workPiece);
        }
        WorkPiece result = workPieceRepository.save(workPiece);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workPiece", workPiece.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workPieces -> get all the workPieces.
     */
    @RequestMapping(value = "/workPieces",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkPiece>> getAllWorkPieces(Pageable pageable)
        throws URISyntaxException {
        Page<WorkPiece> page = workPieceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workPieces");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workPieces/:id -> get the "id" workPiece.
     */
    @RequestMapping(value = "/workPieces/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkPiece> getWorkPiece(@PathVariable Long id) {
        log.debug("REST request to get WorkPiece : {}", id);
        return Optional.ofNullable(workPieceRepository.findOne(id))
            .map(workPiece -> new ResponseEntity<>(
                workPiece,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workPieces/:id -> delete the "id" workPiece.
     */
    @RequestMapping(value = "/workPieces/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkPiece(@PathVariable Long id) {
        log.debug("REST request to delete WorkPiece : {}", id);
        workPieceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workPiece", id.toString())).build();
    }
}
