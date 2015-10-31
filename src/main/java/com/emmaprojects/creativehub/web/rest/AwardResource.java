package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.Award;
import com.emmaprojects.creativehub.repository.AwardRepository;
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
 * REST controller for managing Award.
 */
@RestController
@RequestMapping("/api")
public class AwardResource {

    private final Logger log = LoggerFactory.getLogger(AwardResource.class);

    @Inject
    private AwardRepository awardRepository;

    /**
     * POST  /awards -> Create a new award.
     */
    @RequestMapping(value = "/awards",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Award> createAward(@RequestBody Award award) throws URISyntaxException {
        log.debug("REST request to save Award : {}", award);
        if (award.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new award cannot already have an ID").body(null);
        }
        Award result = awardRepository.save(award);
        return ResponseEntity.created(new URI("/api/awards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("award", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /awards -> Updates an existing award.
     */
    @RequestMapping(value = "/awards",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Award> updateAward(@RequestBody Award award) throws URISyntaxException {
        log.debug("REST request to update Award : {}", award);
        if (award.getId() == null) {
            return createAward(award);
        }
        Award result = awardRepository.save(award);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("award", award.getId().toString()))
            .body(result);
    }

    /**
     * GET  /awards -> get all the awards.
     */
    @RequestMapping(value = "/awards",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Award>> getAllAwards(Pageable pageable)
        throws URISyntaxException {
        Page<Award> page = awardRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/awards");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /awards/:id -> get the "id" award.
     */
    @RequestMapping(value = "/awards/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Award> getAward(@PathVariable Long id) {
        log.debug("REST request to get Award : {}", id);
        return Optional.ofNullable(awardRepository.findOne(id))
            .map(award -> new ResponseEntity<>(
                award,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /awards/:id -> delete the "id" award.
     */
    @RequestMapping(value = "/awards/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAward(@PathVariable Long id) {
        log.debug("REST request to delete Award : {}", id);
        awardRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("award", id.toString())).build();
    }
}
