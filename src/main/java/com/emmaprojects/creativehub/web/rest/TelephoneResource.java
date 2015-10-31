package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.Telephone;
import com.emmaprojects.creativehub.repository.TelephoneRepository;
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
 * REST controller for managing Telephone.
 */
@RestController
@RequestMapping("/api")
public class TelephoneResource {

    private final Logger log = LoggerFactory.getLogger(TelephoneResource.class);

    @Inject
    private TelephoneRepository telephoneRepository;

    /**
     * POST  /telephones -> Create a new telephone.
     */
    @RequestMapping(value = "/telephones",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Telephone> createTelephone(@RequestBody Telephone telephone) throws URISyntaxException {
        log.debug("REST request to save Telephone : {}", telephone);
        if (telephone.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new telephone cannot already have an ID").body(null);
        }
        Telephone result = telephoneRepository.save(telephone);
        return ResponseEntity.created(new URI("/api/telephones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("telephone", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /telephones -> Updates an existing telephone.
     */
    @RequestMapping(value = "/telephones",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Telephone> updateTelephone(@RequestBody Telephone telephone) throws URISyntaxException {
        log.debug("REST request to update Telephone : {}", telephone);
        if (telephone.getId() == null) {
            return createTelephone(telephone);
        }
        Telephone result = telephoneRepository.save(telephone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("telephone", telephone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /telephones -> get all the telephones.
     */
    @RequestMapping(value = "/telephones",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Telephone>> getAllTelephones(Pageable pageable)
        throws URISyntaxException {
        Page<Telephone> page = telephoneRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/telephones");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /telephones/:id -> get the "id" telephone.
     */
    @RequestMapping(value = "/telephones/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Telephone> getTelephone(@PathVariable Long id) {
        log.debug("REST request to get Telephone : {}", id);
        return Optional.ofNullable(telephoneRepository.findOne(id))
            .map(telephone -> new ResponseEntity<>(
                telephone,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /telephones/:id -> delete the "id" telephone.
     */
    @RequestMapping(value = "/telephones/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTelephone(@PathVariable Long id) {
        log.debug("REST request to delete Telephone : {}", id);
        telephoneRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("telephone", id.toString())).build();
    }
}
