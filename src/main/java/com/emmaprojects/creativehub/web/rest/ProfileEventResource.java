package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.ProfileEvent;
import com.emmaprojects.creativehub.repository.ProfileEventRepository;
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
 * REST controller for managing ProfileEvent.
 */
@RestController
@RequestMapping("/api")
public class ProfileEventResource {

    private final Logger log = LoggerFactory.getLogger(ProfileEventResource.class);

    @Inject
    private ProfileEventRepository profileEventRepository;

    /**
     * POST  /profileEvents -> Create a new profileEvent.
     */
    @RequestMapping(value = "/profileEvents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProfileEvent> createProfileEvent(@RequestBody ProfileEvent profileEvent) throws URISyntaxException {
        log.debug("REST request to save ProfileEvent : {}", profileEvent);
        if (profileEvent.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new profileEvent cannot already have an ID").body(null);
        }
        ProfileEvent result = profileEventRepository.save(profileEvent);
        return ResponseEntity.created(new URI("/api/profileEvents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("profileEvent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profileEvents -> Updates an existing profileEvent.
     */
    @RequestMapping(value = "/profileEvents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProfileEvent> updateProfileEvent(@RequestBody ProfileEvent profileEvent) throws URISyntaxException {
        log.debug("REST request to update ProfileEvent : {}", profileEvent);
        if (profileEvent.getId() == null) {
            return createProfileEvent(profileEvent);
        }
        ProfileEvent result = profileEventRepository.save(profileEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("profileEvent", profileEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /profileEvents -> get all the profileEvents.
     */
    @RequestMapping(value = "/profileEvents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ProfileEvent>> getAllProfileEvents(Pageable pageable)
        throws URISyntaxException {
        Page<ProfileEvent> page = profileEventRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/profileEvents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /profileEvents/:id -> get the "id" profileEvent.
     */
    @RequestMapping(value = "/profileEvents/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProfileEvent> getProfileEvent(@PathVariable Long id) {
        log.debug("REST request to get ProfileEvent : {}", id);
        return Optional.ofNullable(profileEventRepository.findOne(id))
            .map(profileEvent -> new ResponseEntity<>(
                profileEvent,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /profileEvents/:id -> delete the "id" profileEvent.
     */
    @RequestMapping(value = "/profileEvents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProfileEvent(@PathVariable Long id) {
        log.debug("REST request to delete ProfileEvent : {}", id);
        profileEventRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("profileEvent", id.toString())).build();
    }
}
