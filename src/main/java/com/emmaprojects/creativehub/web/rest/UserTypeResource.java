package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.UserType;
import com.emmaprojects.creativehub.repository.UserTypeRepository;
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
 * REST controller for managing UserType.
 */
@RestController
@RequestMapping("/api")
public class UserTypeResource {

    private final Logger log = LoggerFactory.getLogger(UserTypeResource.class);

    @Inject
    private UserTypeRepository userTypeRepository;

    /**
     * POST  /userTypes -> Create a new userType.
     */
    @RequestMapping(value = "/userTypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserType> createUserType(@RequestBody UserType userType) throws URISyntaxException {
        log.debug("REST request to save UserType : {}", userType);
        if (userType.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new userType cannot already have an ID").body(null);
        }
        UserType result = userTypeRepository.save(userType);
        return ResponseEntity.created(new URI("/api/userTypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userTypes -> Updates an existing userType.
     */
    @RequestMapping(value = "/userTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserType> updateUserType(@RequestBody UserType userType) throws URISyntaxException {
        log.debug("REST request to update UserType : {}", userType);
        if (userType.getId() == null) {
            return createUserType(userType);
        }
        UserType result = userTypeRepository.save(userType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userType", userType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userTypes -> get all the userTypes.
     */
    @RequestMapping(value = "/userTypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserType>> getAllUserTypes(Pageable pageable)
        throws URISyntaxException {
        Page<UserType> page = userTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userTypes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userTypes/:id -> get the "id" userType.
     */
    @RequestMapping(value = "/userTypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserType> getUserType(@PathVariable Long id) {
        log.debug("REST request to get UserType : {}", id);
        return Optional.ofNullable(userTypeRepository.findOne(id))
            .map(userType -> new ResponseEntity<>(
                userType,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userTypes/:id -> delete the "id" userType.
     */
    @RequestMapping(value = "/userTypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserType(@PathVariable Long id) {
        log.debug("REST request to delete UserType : {}", id);
        userTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userType", id.toString())).build();
    }
}
