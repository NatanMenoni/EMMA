package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.ArtWorkPiece;
import com.emmaprojects.creativehub.repository.ArtWorkPieceRepository;
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
 * REST controller for managing ArtWorkPiece.
 */
@RestController
@RequestMapping("/api")
public class ArtWorkPieceResource {

    private final Logger log = LoggerFactory.getLogger(ArtWorkPieceResource.class);

    @Inject
    private ArtWorkPieceRepository artWorkPieceRepository;

    /**
     * POST  /artWorkPieces -> Create a new artWorkPiece.
     */
    @RequestMapping(value = "/artWorkPieces",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArtWorkPiece> createArtWorkPiece(@RequestBody ArtWorkPiece artWorkPiece) throws URISyntaxException {
        log.debug("REST request to save ArtWorkPiece : {}", artWorkPiece);
        if (artWorkPiece.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new artWorkPiece cannot already have an ID").body(null);
        }
        ArtWorkPiece result = artWorkPieceRepository.save(artWorkPiece);
        return ResponseEntity.created(new URI("/api/artWorkPieces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("artWorkPiece", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /artWorkPieces -> Updates an existing artWorkPiece.
     */
    @RequestMapping(value = "/artWorkPieces",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArtWorkPiece> updateArtWorkPiece(@RequestBody ArtWorkPiece artWorkPiece) throws URISyntaxException {
        log.debug("REST request to update ArtWorkPiece : {}", artWorkPiece);
        if (artWorkPiece.getId() == null) {
            return createArtWorkPiece(artWorkPiece);
        }
        ArtWorkPiece result = artWorkPieceRepository.save(artWorkPiece);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("artWorkPiece", artWorkPiece.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artWorkPieces -> get all the artWorkPieces.
     */
    @RequestMapping(value = "/artWorkPieces",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ArtWorkPiece>> getAllArtWorkPieces(Pageable pageable)
        throws URISyntaxException {
        Page<ArtWorkPiece> page = artWorkPieceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/artWorkPieces");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /artWorkPieces/:id -> get the "id" artWorkPiece.
     */
    @RequestMapping(value = "/artWorkPieces/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArtWorkPiece> getArtWorkPiece(@PathVariable Long id) {
        log.debug("REST request to get ArtWorkPiece : {}", id);
        return Optional.ofNullable(artWorkPieceRepository.findOne(id))
            .map(artWorkPiece -> new ResponseEntity<>(
                artWorkPiece,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /artWorkPieces/:id -> delete the "id" artWorkPiece.
     */
    @RequestMapping(value = "/artWorkPieces/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteArtWorkPiece(@PathVariable Long id) {
        log.debug("REST request to delete ArtWorkPiece : {}", id);
        artWorkPieceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("artWorkPiece", id.toString())).build();
    }
}
