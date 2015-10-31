package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.WorkExperience;
import com.emmaprojects.creativehub.repository.WorkExperienceRepository;
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
 * REST controller for managing WorkExperience.
 */
@RestController
@RequestMapping("/api")
public class WorkExperienceResource {

    private final Logger log = LoggerFactory.getLogger(WorkExperienceResource.class);

    @Inject
    private WorkExperienceRepository workExperienceRepository;

    /**
     * POST  /workExperiences -> Create a new workExperience.
     */
    @RequestMapping(value = "/workExperiences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkExperience> createWorkExperience(@RequestBody WorkExperience workExperience) throws URISyntaxException {
        log.debug("REST request to save WorkExperience : {}", workExperience);
        if (workExperience.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new workExperience cannot already have an ID").body(null);
        }
        WorkExperience result = workExperienceRepository.save(workExperience);
        return ResponseEntity.created(new URI("/api/workExperiences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workExperience", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workExperiences -> Updates an existing workExperience.
     */
    @RequestMapping(value = "/workExperiences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkExperience> updateWorkExperience(@RequestBody WorkExperience workExperience) throws URISyntaxException {
        log.debug("REST request to update WorkExperience : {}", workExperience);
        if (workExperience.getId() == null) {
            return createWorkExperience(workExperience);
        }
        WorkExperience result = workExperienceRepository.save(workExperience);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workExperience", workExperience.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workExperiences -> get all the workExperiences.
     */
    @RequestMapping(value = "/workExperiences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<WorkExperience>> getAllWorkExperiences(Pageable pageable)
        throws URISyntaxException {
        Page<WorkExperience> page = workExperienceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workExperiences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workExperiences/:id -> get the "id" workExperience.
     */
    @RequestMapping(value = "/workExperiences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkExperience> getWorkExperience(@PathVariable Long id) {
        log.debug("REST request to get WorkExperience : {}", id);
        return Optional.ofNullable(workExperienceRepository.findOne(id))
            .map(workExperience -> new ResponseEntity<>(
                workExperience,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workExperiences/:id -> delete the "id" workExperience.
     */
    @RequestMapping(value = "/workExperiences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkExperience(@PathVariable Long id) {
        log.debug("REST request to delete WorkExperience : {}", id);
        workExperienceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workExperience", id.toString())).build();
    }
}
