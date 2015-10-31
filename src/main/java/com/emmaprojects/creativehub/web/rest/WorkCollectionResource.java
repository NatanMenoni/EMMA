package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.WorkCollection;
import com.emmaprojects.creativehub.repository.WorkCollectionRepository;
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
 * REST controller for managing WorkCollection.
 */
@RestController
@RequestMapping("/api")
public class WorkCollectionResource {

    private final Logger log = LoggerFactory.getLogger(WorkCollectionResource.class);

    @Inject
    private WorkCollectionRepository workCollectionRepository;

    /**
     * POST  /workCollections -> Create a new workCollection.
     */
    @RequestMapping(value = "/workCollections",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkCollection> createWorkCollection(@RequestBody WorkCollection workCollection) throws URISyntaxException {
        log.debug("REST request to save WorkCollection : {}", workCollection);
        if (workCollection.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new workCollection cannot already have an ID").body(null);
        }
        WorkCollection result = workCollectionRepository.save(workCollection);
        return ResponseEntity.created(new URI("/api/workCollections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workCollection", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workCollections -> Updates an existing workCollection.
     */
    @RequestMapping(value = "/workCollections",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkCollection> updateWorkCollection(@RequestBody WorkCollection workCollection) throws URISyntaxException {
        log.debug("REST request to update WorkCollection : {}", workCollection);
        if (workCollection.getId() == null) {
            return createWorkCollection(workCollection);
        }
        WorkCollection result = workCollectionRepository.save(workCollection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workCollection", workCollection.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workCollections -> get all the workCollections.
     */
    @RequestMapping(value = "/workCollections",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkCollection>> getAllWorkCollections(Pageable pageable)
        throws URISyntaxException {
        Page<WorkCollection> page = workCollectionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workCollections");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workCollections/:id -> get the "id" workCollection.
     */
    @RequestMapping(value = "/workCollections/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkCollection> getWorkCollection(@PathVariable Long id) {
        log.debug("REST request to get WorkCollection : {}", id);
        return Optional.ofNullable(workCollectionRepository.findOne(id))
            .map(workCollection -> new ResponseEntity<>(
                workCollection,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workCollections/:id -> delete the "id" workCollection.
     */
    @RequestMapping(value = "/workCollections/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkCollection(@PathVariable Long id) {
        log.debug("REST request to delete WorkCollection : {}", id);
        workCollectionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workCollection", id.toString())).build();
    }
}
