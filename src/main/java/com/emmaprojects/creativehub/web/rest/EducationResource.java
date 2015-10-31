package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.Education;
import com.emmaprojects.creativehub.repository.EducationRepository;
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
 * REST controller for managing Education.
 */
@RestController
@RequestMapping("/api")
public class EducationResource {

    private final Logger log = LoggerFactory.getLogger(EducationResource.class);

    @Inject
    private EducationRepository educationRepository;

    /**
     * POST  /educations -> Create a new education.
     */
    @RequestMapping(value = "/educations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Education> createEducation(@RequestBody Education education) throws URISyntaxException {
        log.debug("REST request to save Education : {}", education);
        if (education.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new education cannot already have an ID").body(null);
        }
        Education result = educationRepository.save(education);
        return ResponseEntity.created(new URI("/api/educations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("education", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /educations -> Updates an existing education.
     */
    @RequestMapping(value = "/educations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Education> updateEducation(@RequestBody Education education) throws URISyntaxException {
        log.debug("REST request to update Education : {}", education);
        if (education.getId() == null) {
            return createEducation(education);
        }
        Education result = educationRepository.save(education);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("education", education.getId().toString()))
            .body(result);
    }

    /**
     * GET  /educations -> get all the educations.
     */
    @RequestMapping(value = "/educations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Education>> getAllEducations(Pageable pageable)
        throws URISyntaxException {
        Page<Education> page = educationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/educations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /educations/:id -> get the "id" education.
     */
    @RequestMapping(value = "/educations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Education> getEducation(@PathVariable Long id) {
        log.debug("REST request to get Education : {}", id);
        return Optional.ofNullable(educationRepository.findOne(id))
            .map(education -> new ResponseEntity<>(
                education,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /educations/:id -> delete the "id" education.
     */
    @RequestMapping(value = "/educations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        log.debug("REST request to delete Education : {}", id);
        educationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("education", id.toString())).build();
    }
}
